package pl.hubertmaka.culinaryagent.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.AdvisorParams;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for setting up the ChatClient beans with specific parameters for different agents.
 */
@Configuration
public class AgentConfig {
    /** Logger for logging information and debugging purposes. */
    private static final Logger log = LoggerFactory.getLogger(AgentConfig.class);
    /** The prompt content for the extractor personality, loaded from the classpath resource. */
    @Value("classpath:prompts/recipe-extractor-agent-personality.md")
    private String recipeExtractorAgentPersonality;
    @Value("classpath:prompts/recipe-chat-agent-personality.md")
    private String recipeChatAgentPersonality;

    /**
     * Bean definition for the ChatClient used in recipe extraction.
     * This method configures the ChatClient with a default system prompt and enables native structured output.
     *
     * @param builder the ChatClient.Builder used to build the ChatClient instance
     * @return a configured ChatClient instance for recipe extraction
     */
    @Bean
    public ChatClient recipeExtractorAgent(ChatClient.Builder builder) {
        log.info("Creating chat client for extractor agent");
        return builder
                .defaultSystem(recipeExtractorAgentPersonality)
                .defaultAdvisors(AdvisorParams.ENABLE_NATIVE_STRUCTURED_OUTPUT)
                .build();
    }

    /**
     * Bean definition for the ChatClient used in recipe chat interactions.
     * This method configures the ChatClient with a default system prompt and enables native structured output.
     *
     * @param builder the ChatClient.Builder used to build the ChatClient instance
     * @return a configured ChatClient instance for recipe chat interactions
     */
    @Bean
    public ChatClient recipeChatAgent(ChatClient.Builder builder) {
        log.info("Creating chat client for chat agent");
        return builder
                .defaultSystem(recipeChatAgentPersonality)
                .build();
    }
}
