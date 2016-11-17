package com.apischan.aws.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.apischan.aws.lambda.exception.ImageProcessingException;
import com.apischan.aws.lambda.image.ImageContent;
import com.apischan.aws.lambda.image.ImageMetadata;
import com.apischan.aws.lambda.image.ImageOperations;
import com.apischan.aws.lambda.s3.S3NamingProvider;
import com.apischan.aws.lambda.s3.S3Helper;
import com.apischan.aws.lambda.s3.S3RecordParser;
import java.io.IOException;
import java.io.InputStream;

import static com.amazonaws.services.s3.event.S3EventNotification.*;

public class S3EventProcessorCreateThumbnail implements RequestHandler<S3Event, String> {

    public String handleRequest(S3Event s3event, Context context) {
        System.out.println("Handling request:\n" + s3event.toJson());
        try {
            S3EventNotificationRecord record = s3event.getRecords().get(0);

            S3RecordParser s3RecordParser = new S3RecordParser(record);
            S3NamingProvider namingProvider = new S3NamingProvider(s3RecordParser);

            String imageType = s3RecordParser.getImageType();

            // Download the image from S3 into a stream
            S3Helper s3Helper = new S3Helper(namingProvider);
            System.out.println("Downloading image.");
            InputStream objectData = s3Helper.downloadObject();

            ImageMetadata imageData = new ImageMetadata(imageType);
            ImageContent imageContent = new ImageContent(imageData, new ImageOperations(objectData));

            // Set Content-Length and Content-Type
            ObjectMetadata meta = imageContent.getObjectMetadata();

            // Uploading to S3 destination bucket
            System.out.println(
                    String.format("Writing to: %s/%s", namingProvider.getDstBucket(), namingProvider.getDstKey())
            );
            s3Helper.saveObject(imageContent.getInputStream(), meta);

            System.out.println(
                    String.format("Successfully resized %s/%s  and uploaded to %s/%s",
                        namingProvider.getSrcBucket(), namingProvider.getSrcKey(),
                        namingProvider.getDstBucket(), namingProvider.getDstKey()
                    )
            );
            return "Ok";
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ImageProcessingException e) {
            System.out.println(e.getMessage());
            return "";
        }
    }

}
