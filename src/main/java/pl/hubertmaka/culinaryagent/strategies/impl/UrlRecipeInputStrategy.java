package pl.hubertmaka.culinaryagent.strategies.impl;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import pl.hubertmaka.culinaryagent.domain.dtos.RecipeDataDto;
import pl.hubertmaka.culinaryagent.domain.enums.RecipeSource;
import pl.hubertmaka.culinaryagent.strategies.RecipeInputStrategy;

/**
 * Strategy implementation for handling URL-based recipe inputs.
 * This strategy supports the RecipeSource.URL type and creates a UserMessage
 * containing the extracted text content from the provided URL.
 */
@Component
public class UrlRecipeInputStrategy implements RecipeInputStrategy {
    /** Logger for logging information and debugging purposes. */
    private final static Logger log = LoggerFactory.getLogger(UrlRecipeInputStrategy.class);

    /** WebClient instance for making HTTP requests to fetch content from URLs. */
    private final WebClient webClient;

    /**
     * Constructor for UrlRecipeInputStrategy that initializes the WebClient with a default user agent header.
     *
     * @param webClient the WebClient to be used for fetching content from URLs, injected by Spring
     */
    public UrlRecipeInputStrategy(WebClient webClient) {
        this.webClient = webClient;
    }

    /**
     * Implements the supports method to check if the given recipe source is of type URL.
     *
     * @param source the recipe source to check
     * @return true if the source is RecipeSource.URL, false otherwise
     */
    @Override
    public boolean supports(RecipeSource source) {
        return source == RecipeSource.URL;
    }

    /**
     * Implements the createMessage method to create a UserMessage containing the extracted text content from the provided URL.
     *
     * @param recipeData the data of the recipe to create a message from
     * @return a UserMessage representing the extracted text content from the URL
     */
    @Override
    public UserMessage createMessage(RecipeDataDto recipeData) {
        return UserMessage.builder()
                .text(extractContent(recipeData.content()))
                .build();
    }

    /**
     * Extracts the text content from the provided URL by making an HTTP GET request and parsing the HTML response.
     *
     * @param url the URL from which to extract content
     * @return a String containing the extracted text content from the URL
     */
    protected String extractContent(String url) {
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(String.class)
                .map(this::extractTextFromHtml)
                .block();
    }

    /**
     * Extracts the text content from the given HTML content by parsing it and removing unnecessary elements.
     *
     * @param htmlContent the HTML content to extract text from
     * @return a String containing the extracted text content from the HTML
     */
    private String extractTextFromHtml(String htmlContent) {
        Document document = Jsoup.parse(htmlContent);
        document.select("script, style, meta, link").remove();
        return document.body().text();
    }

}
