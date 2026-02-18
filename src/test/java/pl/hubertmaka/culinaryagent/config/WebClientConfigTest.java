package pl.hubertmaka.culinaryagent.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WebClientConfigTest {

    private WebClientConfig webClientConfig;

    @BeforeEach
    void setUp() {
        webClientConfig = new WebClientConfig();
    }

    @Test
    @DisplayName("Test if WebClientConfig is initialized correctly")
    void whenInitialized_thenNotNull() {
        // Given

        // Then
        assertNotNull(webClientConfig);
    }

    @Test
    @DisplayName("Test if WebClient bean is created")
    void whenWebClient_thenNotNull() {
        // Given

        // When
        var webClient = webClientConfig.webClient();

        // Then
        assertNotNull(webClient);
    }
}