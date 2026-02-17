package pl.hubertmaka.culinaryagent.strategies.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.content.Media;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;
import pl.hubertmaka.culinaryagent.domain.dtos.RecipeDataDto;
import pl.hubertmaka.culinaryagent.domain.enums.FileExtension;
import pl.hubertmaka.culinaryagent.domain.enums.RecipeSource;
import pl.hubertmaka.culinaryagent.strategies.RecipeInputStrategy;

import java.util.Base64;
import java.util.List;

/**
 * Strategy implementation for handling image-based recipe inputs.
 * This strategy supports the RecipeSource.IMAGE type and creates a UserMessage
 * containing the image as media content.
 */
@Component
public class ImageRecipeInputStrategy implements RecipeInputStrategy {
    /** Logger for logging information and debugging purposes. */
    private final static Logger log = LoggerFactory.getLogger(ImageRecipeInputStrategy.class);

    /**
     * Implements the supports method to check if the given recipe source is of type IMAGE.
     *
     * @param source the recipe source to check
     * @return true if the source is RecipeSource.IMAGE, false otherwise
     */
    @Override
    public boolean supports(RecipeSource source) {
        log.info("Checking source support for image recipe input...");
        return source == RecipeSource.IMAGE;
    }

    /**
     * Implements the createMessage method to create a UserMessage containing the image as media content.
     *
     * @param recipeData the data of the recipe to create a message from
     * @return a UserMessage representing the image recipe data content
     */
    @Override
    public UserMessage createMessage(RecipeDataDto recipeData) {
        log.info("Creating UserMessage for image recipe input...");
        MimeType mimeType = getMimeType(recipeData.fileExtension());
        Resource image = decodeImage(recipeData.content());
        return UserMessage.builder()
                .text("Extract text from given image")
                .media(List.of(new Media(mimeType, image)))
                .build();
    }

    /**
     * Decodes a base64 encoded image string into a Resource that can be used as media content in a UserMessage.
     *
     * @param base64EncodedImage the base64 encoded image string to decode
     * @return a Resource containing the decoded image data
     */
    private Resource decodeImage(String base64EncodedImage) {
        log.info("Decoding image from base64 encoded image...");
        String cleanBase64Image = base64EncodedImage;
        if (base64EncodedImage.contains(",")) {
            log.info("Decoding image from base64 encoded image...");
            cleanBase64Image = base64EncodedImage.split(",")[1];
        }
        byte[] decodedBytes = Base64.getDecoder().decode(cleanBase64Image);
        return new ByteArrayResource(decodedBytes);
    }

    /**
     * Determines the MIME type of the image based on its file extension.
     *
     * @param extension the file extension of the image
     * @return the corresponding MIME type for the given file extension
     */
    private MimeType getMimeType(FileExtension extension) {
        log.info("Get mime type for image recipe input...");
        return switch (extension) {
            case JPEG, JPG -> MimeTypeUtils.IMAGE_JPEG;
            case PNG -> MimeTypeUtils.IMAGE_PNG;
        };
    }
}
