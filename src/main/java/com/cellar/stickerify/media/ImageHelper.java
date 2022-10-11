package com.cellar.stickerify.media;

import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Mode;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public final class ImageHelper {

    /**
     * @see <a href="https://core.telegram.org/stickers#static-stickers-and-emoji">Telegram documentation</a>
     */
    private static final int MAX_ALLOWED_SIZE = 512;

    /**
     * Given an image file, it converts it to a png file of the proper dimension (max 512 x 512).
     *
     * @param file the file to convert to png
     * @return a resized and converted png image
     * @throws TelegramApiException if passed-in file doesn't represent a valid image
     */
    public static File convertToPng(File file) throws TelegramApiException {
        try {
            return createPngFile(resizeImage(ImageIO.read(file)));
        } catch (IOException e) {
            throw new TelegramApiException("An unexpected error occurred trying to create resulting image", e);
        }
    }

    /**
     * Given an image, it returns its resized version with sides of max 512 pixels each.
     *
     * @param originalImage the image to be resized
     * @return resized image
     */
    private static BufferedImage resizeImage(BufferedImage originalImage) {
        if (originalImage.getWidth() >= originalImage.getHeight()) {
            return Scalr.resize(originalImage, Mode.FIT_TO_WIDTH, MAX_ALLOWED_SIZE);
        } else {
            return Scalr.resize(originalImage, Mode.FIT_TO_HEIGHT, MAX_ALLOWED_SIZE);
        }
    }

    /**
     * Creates a new <i>.png</i> file from passed-in {@code image}.
     *
     * @param image the image to convert to png
     * @return png image
     * @throws IOException if an error occurs creating the png
     */
    private static File createPngFile(BufferedImage image) throws IOException {
        var pngImage = File.createTempFile("Stickerify-", ".png");
        ImageIO.write(image, "png", pngImage);

        return pngImage;
    }

    private ImageHelper() {
        throw new UnsupportedOperationException();
    }
}
