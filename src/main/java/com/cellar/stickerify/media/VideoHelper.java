package com.cellar.stickerify.media;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.schild.jave.Encoder;
import ws.schild.jave.EncoderException;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingAttributes;
import ws.schild.jave.encode.VideoAttributes;
import ws.schild.jave.filters.ScaleFilter;
import ws.schild.jave.filters.helpers.ForceOriginalAspectRatio;
import ws.schild.jave.info.VideoSize;

import java.io.File;
import java.util.UUID;

public final class VideoHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(VideoHelper.class);

    private static final String WEBM_EXTENSION = "webm";

    private static final VideoSize MAX_ALLOWED_VIDEO_SIZE = new VideoSize(512, 512);

    /**
     * Given an animated image file(gif or gifv), it converts it to a webm file of the proper dimension (max 512 x 512).
     *
     * @param file the file to convert to webm
     * @return a resized and converted webm video
     */
    public static File convertToWebm(File file) {
        try {
            return createWebmFile(file);
        } catch (EncoderException e) {
            LOGGER.error("Error while converting image to webm!", e);
            return file;
        }
    }

    private static File createWebmFile(File gifFile) throws EncoderException {
        var encodingAttributes = new EncodingAttributes();

        var video = new VideoAttributes()
                .setPixelFormat("yuv420p")
                .setCodec("vp9")
                .setFrameRate(30)
                .setSize(MAX_ALLOWED_VIDEO_SIZE);

        encodingAttributes.setDuration(3.0f)
                .setAudioAttributes(null)
                .setLoop(true)
                .setVideoAttributes(video);

        final var output = new File(UUID.randomUUID() + "." + WEBM_EXTENSION);
        var encoder = new Encoder();
        encoder.encode(new MultimediaObject(gifFile), output, encodingAttributes);

        return output;
    }

    private VideoHelper() {
        throw new UnsupportedOperationException();
    }
}
