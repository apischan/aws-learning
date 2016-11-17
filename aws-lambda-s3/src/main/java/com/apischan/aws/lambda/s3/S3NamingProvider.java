package com.apischan.aws.lambda.s3;

import com.apischan.aws.lambda.exception.ImageProcessingException;

import java.io.IOException;

public class S3NamingProvider {

    private final String srcBucket;
    private final String srcKey;

    private final String dstBucket;
    private final String dstKey;

    public S3NamingProvider(S3RecordParser s3RecordParser) throws IOException, ImageProcessingException {
        this.srcBucket = s3RecordParser.getBucketName();
        this.srcKey = s3RecordParser.getSrcKey();
        this.dstBucket = srcBucket + "resized";
        this.dstKey = "resized-" + srcKey;
        // Sanity check: validate that source and destination are different
        // buckets.
        if (srcBucket.equals(dstBucket)) {
            throw new ImageProcessingException("Destination bucket must not match source bucket.");
        }
    }

    public String getSrcBucket() {
        return srcBucket;
    }

    public String getSrcKey() {
        return srcKey;
    }

    public String getDstBucket() {
        return dstBucket;
    }

    public String getDstKey() {
        return dstKey;
    }

}
