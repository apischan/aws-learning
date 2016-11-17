package com.apischan.aws.lambda.exception;

public class ImageProcessingException extends Exception {

    public ImageProcessingException(String message) {
        super(message);
    }

    public ImageProcessingException(String message, Object ... args) {
        super(String.format(message, args));
    }
}
