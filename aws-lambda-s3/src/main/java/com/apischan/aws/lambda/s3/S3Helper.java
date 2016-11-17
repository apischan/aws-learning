package com.apischan.aws.lambda.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;

import java.io.InputStream;

public class S3Helper {

    private AmazonS3 s3Client = new AmazonS3Client();

    private S3NamingProvider namingProvider;

    public S3Helper(S3NamingProvider namingProvider) {
        this.namingProvider = namingProvider;
    }

    public InputStream downloadObject() {
        S3Object s3Object = s3Client.getObject(new GetObjectRequest(namingProvider.getSrcBucket(), namingProvider.getSrcKey()));
        return s3Object.getObjectContent();
    }

    public void saveObject(InputStream is, ObjectMetadata meta) {
        s3Client.putObject(namingProvider.getDstBucket(), namingProvider.getDstKey(), is, meta);
    }

}
