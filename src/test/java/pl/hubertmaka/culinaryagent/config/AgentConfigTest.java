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
import org.springframework.core.io.ByteArrayResource;
import org.springframework.test.util.ReflectionTestUtils;
import pl.hubertmaka.culinaryagent.mappers.impl.RecipeSchemaMapper;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AgentConfigTest {
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private ChatClient.Builder chatClientBuilder;
    @Mock
    private RecipeSchemaMapper converter;

    @InjectMocks
    private AgentConfig agentConfig;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(agentConfig, "recipeExtractorAgentPersonality",
                new ByteArrayResource("Test extractor personality".getBytes(StandardCharsets.UTF_8)));
        ReflectionTestUtils.setField(agentConfig, "recipeChatAgentPersonality",
                new ByteArrayResource("Test chat personality".getBytes(StandardCharsets.UTF_8)));
        ReflectionTestUtils.setField(agentConfig, "agentUserInstruction",
                new ByteArrayResource("Test agent instruction".getBytes(StandardCharsets.UTF_8)));
        ReflectionTestUtils.setField(agentConfig, "extractorUserInstruction",
                new ByteArrayResource("Test extractor instruction".getBytes(StandardCharsets.UTF_8)));
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
        when(converter.getFormat()).thenReturn("test-format");

        // When
        var agentModel = agentConfig.recipeExtractorAgent(chatClientBuilder, converter);

        // Then
        assertNotNull(agentModel);
    }
}