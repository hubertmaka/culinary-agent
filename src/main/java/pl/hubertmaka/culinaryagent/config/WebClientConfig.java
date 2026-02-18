package pl.hubertmaka.culinaryagent.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Configuration class for setting up the WebClient bean with a simulated user agent header.
 * This configuration ensures that all HTTP requests made using the WebClient will include a user agent string
 * that mimics a real browser, which can help in avoiding blocks from servers that restrict non-browser requests.
 */
@Configuration
public class WebClientConfig {
    /** Logger for logging information and debugging purposes. */
    private static final Logger log = LoggerFactory.getLogger(WebClientConfig.class);
    /** A simulated user agent string to mimic a real browser when making HTTP requests. */
    private final static String SIMULATED_USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:124.0) Gecko/20100101 Firefox/124.0";

    /**
     * Bean definition for the WebClient used in the application. This method configures the WebClient with a default user agent header.
     *
     * @return a configured WebClient instance with a simulated user agent header
     */
    @Bean
    public WebClient webClient() {
        log.info("Creating WebClient bean");
        return WebClient.builder()
                .defaultHeader(HttpHeaders.USER_AGENT, SIMULATED_USER_AGENT)
                .build();
    }
}
