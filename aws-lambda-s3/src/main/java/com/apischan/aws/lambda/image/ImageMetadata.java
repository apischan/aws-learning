package com.apischan.aws.lambda.image;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.apischan.aws.lambda.exception.ImageProcessingException;

import java.util.stream.Stream;

public class ImageMetadata {

    private final ImageFormat imageFormat;

    public ImageMetadata(String imgType) throws ImageProcessingException {
        this.imageFormat = ImageFormat.getImageFormat(imgType);
        if (ImageFormat.UNKNOWN.equals(imageFormat)) {
            throw new ImageProcessingException("Skipping non-image %s", imgType);
        }
    }

    String getImageType() {
        return imageFormat.type;
    }

    String getImageMime() {
        return imageFormat.mime;
    }

    private enum ImageFormat {
        JPG("jpg", "image/jpeg"),
        PNG("png", "image/png"),
        UNKNOWN("", "");

        String type;
        String mime;

        ImageFormat(String type, String mime) {
            this.type = type;
            this.mime = mime;
        }

        public static ImageFormat getImageFormat(String imgType) {
            return Stream.of(values())
                    .filter(format -> format.type.equals(imgType))
                    .findFirst()
                    .orElse(UNKNOWN);
        }
    }
}
