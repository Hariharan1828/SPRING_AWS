package com.spring.aws_image_upload.profile;

import com.spring.aws_image_upload.bucket.BucketName;
import com.spring.aws_image_upload.filestore.FileStore;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

import static org.apache.http.entity.ContentType.*;

@Service
public class UserProfileService{

    @Autowired
    public UserProfileService(UserProfileDataAccessService userProfileDataAccessService, FileStore fileStore) {
        this.userProfileDataAccessService = userProfileDataAccessService;
        this.fileStore = fileStore;
    }

    List<UserProfile> getUserProfile(){
        return userProfileDataAccessService.getUserProfile();
    }
    private final UserProfileDataAccessService userProfileDataAccessService;
    private final FileStore fileStore;

    public void uploadUserProfileImage(UUID userProfileId, MultipartFile file) throws IOException {
        // 1. Check if image is not empty
        if(file.isEmpty()){
            throw new IllegalStateException("Cannot upload the empty file [ "+ file.getSize()+"]");
        }
        // 2. If file is an image
        if(!Arrays.asList(IMAGE_JPEG, IMAGE_PNG, IMAGE_GIF).contains(file.getContentType())){
            throw new IllegalStateException("File must be an image");
        }

        // 3. The user exists in our database
        userProfileDataAccessService.getUserProfile()
                .stream()
                .filter(userProfile -> userProfile.getUserProfileId().equals(userProfileId))//checking the userId is e1 to userId from client
                .findFirst()
                .orElseThrow(()-> new IllegalStateException(String.format("the user id %s doesnot exist",userProfileId)));

        // 4. Grab some metadata from file if any
        Map<String,String> metaData = new HashMap<>();
        metaData.put("Content-type" , file.getContentType());
        metaData.put("Content Size", String.valueOf(file.getSize()));

        // 5. Store the image in the s3 and update the s3 image link in the database (userProfileImageLink)
        String path  = String.format("%s/%s", BucketName.PROFILE_IMAGE.getBucketName());
        String fileName = String.format("%s-%s", file.getName(), UUID.randomUUID());

        try{
            fileStore.save(path,fileName, Optional.of(metaData),file.getInputStream());
        }catch (IOException e){
            throw new IllegalStateException(e);
        }

    }
}
