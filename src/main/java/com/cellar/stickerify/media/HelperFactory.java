package com.cellar.stickerify.media;

import org.apache.tika.Tika;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class HelperFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(HelperFactory.class);

    private static final String MIME_TYPE_IMAGE = "image/";

    private static final List<String> ANIMATED_STICKER_FORMATS = List.of("gif", "gifv", "webp");

    public static File createSticker(File file) throws TelegramApiException {
        if (!isValidImage(file)) throw new TelegramApiException("Passed-in file is not supported");

        try {
            String mimeType = new Tika().detect(file);

            if(ANIMATED_STICKER_FORMATS.stream().anyMatch(mimeType::endsWith)){
                return VideoHelper.convertToWebm(file);
            }

            return ImageHelper.convertToPng(file);
        } catch (IOException e) {
            throw new TelegramApiException("Error while converting file to Sticker");
        }
    }

    /**
     * Checks if passed-in file represents a valid image.
     *
     * @param file the file sent to the bot
     * @return {@code true} if {@code file} is an image
     */
    private static boolean isValidImage(File file) {
        boolean isValid = false;

        try {
            String mimeType = new Tika().detect(file);
            isValid = mimeType.startsWith(MIME_TYPE_IMAGE);

            LOGGER.info("The file has {} MIME type", mimeType);
        } catch (IOException e) {
            LOGGER.error("Unable to retrieve MIME type for file {}", file.getName());
        }

        return isValid;
    }
}
