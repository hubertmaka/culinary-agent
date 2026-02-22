package pl.hubertmaka.culinaryagent.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RestClientConfigTest {

    private RestClientConfig restClientConfig;

    @BeforeEach
    void setUp() {
        restClientConfig = new RestClientConfig();
    }

    @Test
    @DisplayName("Test if WebClientConfig is initialized correctly")
    void whenInitialized_thenNotNull() {
        // Given

        // Then
        assertNotNull(restClientConfig);
    }

    @Test
    @DisplayName("Test if RestClient bean is created")
    void whenRestClient_thenNotNull() {
        // Given

        // When
        var restClient = restClientConfig.restClient();

        // Then
        assertNotNull(restClient);
    }
}