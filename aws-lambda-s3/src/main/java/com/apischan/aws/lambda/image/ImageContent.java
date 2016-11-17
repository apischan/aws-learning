package com.apischan.aws.lambda.image;

import com.amazonaws.services.s3.model.ObjectMetadata;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImageContent {

    private static final float MAX_WIDTH = 100;
    private static final float MAX_HEIGHT = 100;

    private BufferedImage scaledImage;

    private ByteArrayOutputStream os = new ByteArrayOutputStream();

    private ImageMetadata metadata;

    public ImageContent(ImageMetadata metadata, ImageOperations imageOps) throws IOException {
        this.metadata = metadata;
        BufferedImage sourceImage = imageOps.read();
        this.scaledImage = scaleImage(sourceImage);
        imageOps.write(scaledImage, metadata.getImageType(), os);
    }

    public BufferedImage getScaledImage() {
        return scaledImage;
    }

    public InputStream getInputStream() {
        return new ByteArrayInputStream(os.toByteArray());
    }


    public ObjectMetadata getObjectMetadata() {
        ObjectMetadata meta = new ObjectMetadata();
        meta.setContentLength(os.size());
        meta.setContentType(metadata.getImageMime());
        return meta;
    }

    private BufferedImage scaleImage(BufferedImage srcImage) throws IOException {
        int srcHeight = srcImage.getHeight();
        int srcWidth = srcImage.getWidth();

        // Infer the scaling factor to avoid stretching the image unnaturally
        float scalingFactor = Math.min(MAX_WIDTH / srcWidth, MAX_HEIGHT / srcHeight);
        int width = (int) (scalingFactor * srcWidth);
        int height = (int) (scalingFactor * srcHeight);

        BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = resizedImage.createGraphics();
        // Fill with white before applying semi-transparent (alpha) images
        g.setPaint(Color.white);
        g.fillRect(0, 0, width, height);
        // Simple bilinear resize
        // If you want higher quality algorithms, check this link:
        // https://today.java.net/pub/a/today/2007/04/03/perils-of-image-getscaledinstance.html
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(srcImage, 0, 0, width, height, null);
        g.dispose();
        return resizedImage;
    }
}
