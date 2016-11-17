package com.apischan.aws.lambda.s3;

import com.amazonaws.services.s3.event.S3EventNotification.S3EventNotificationRecord;
import com.apischan.aws.lambda.exception.ImageProcessingException;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class S3RecordParser {

    private static final Pattern pattern = Pattern.compile(".*\\.([^\\.]*)");

    private final String bucketName;
    private final String srcKey;
    private final String imageType;

    public S3RecordParser(S3EventNotificationRecord record) throws IOException, ImageProcessingException {
        this.bucketName = record.getS3().getBucket().getName();
        // Object key may have spaces or unicode non-ASCII characters.
        this.srcKey = URLDecoder.decode(record.getS3().getObject().getKey().replace('+', ' '), "UTF-8");
        Matcher matcher = pattern.matcher(srcKey);
        if (!matcher.matches()) {
            throw new ImageProcessingException("Unable to infer image type for key %s", srcKey);
        }
        this.imageType = matcher.group(1);
    }

    String getBucketName() {
        return bucketName;
    }

    String getSrcKey() throws IOException {
        return srcKey;
    }

    public String getImageType() {
        return imageType;
    }
}
