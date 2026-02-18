package pl.hubertmaka.culinaryagent.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AgentConfigTest {
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private ChatClient.Builder chatClientBuilder;

    @InjectMocks
    private  AgentConfig agentConfig;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(agentConfig, "recipeExtractorAgentPersonality", "Test extractor personality");
        ReflectionTestUtils.setField(agentConfig, "recipeChatAgentPersonality", "Test chat personality");
    }

    @Test
    @DisplayName("Test if AgentConfig is initialized correctly")
    void whenInitialized_thenNotNull() {
        // Given

        // Then
        assertNotNull(agentConfig);
    }

    @Test
    @DisplayName("Test if agentModel bean is created for recipe chat agent")
    void whenAgentModel_thenNotNull() {
        // Given

        // When
        var agentModel = agentConfig.recipeChatAgent(chatClientBuilder);

        // Then
        assertNotNull(agentModel);
    }

    @Test
    @DisplayName("Test if agentModel bean is created for recipe extractor agent")
    void whenAgentModel_thenNotNullForExtractor() {
        // Given

        // When
        var agentModel = agentConfig.recipeExtractorAgent(chatClientBuilder);

        // Then
        assertNotNull(agentModel);
    }
}