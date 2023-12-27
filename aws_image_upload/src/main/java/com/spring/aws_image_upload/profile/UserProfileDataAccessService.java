package com.spring.aws_image_upload.profile;

import com.spring.aws_image_upload.datastore.FakeUserProfileDataStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserProfileDataAccessService {

    @Autowired
    public UserProfileDataAccessService(FakeUserProfileDataStore fakeUserProfileDataStore) {
        this.fakeUserProfileDataStore = fakeUserProfileDataStore;
    }

    List<UserProfile> getUserProfile(){
        return fakeUserProfileDataStore.getUserProfile();
    }

    private final FakeUserProfileDataStore fakeUserProfileDataStore;


}
