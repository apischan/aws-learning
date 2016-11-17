package com.apischan.aws.lambda.image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class ImageOperations {

    private InputStream inputStream;


    public ImageOperations(InputStream imageData) {
        this.inputStream = imageData;
    }

    public BufferedImage read() throws IOException {
        return ImageIO.read(inputStream);
    }

    public void write(BufferedImage resizedImage, String imageType, OutputStream os) throws IOException {
        ImageIO.write(resizedImage, imageType, os);
    }

}
