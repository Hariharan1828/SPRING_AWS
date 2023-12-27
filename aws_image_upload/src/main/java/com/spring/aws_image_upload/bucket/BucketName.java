package com.spring.aws_image_upload.bucket;

public enum BucketName {
    PROFILE_IMAGE("hari-123-final");

    BucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    private final String bucketName;

    public String getBucketName() {
        return bucketName;
    }

}
