package pl.hubertmaka.culinaryagent.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.AdvisorParams;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.hubertmaka.culinaryagent.mappers.impl.RecipeSchemaMapper;

/**
 * Configuration class for setting up ChatClient beans for the culinary agent application.
 */
@Configuration
public class AgentConfig {
    /** Logger for logging information and debugging purposes. */
    private static final Logger log = LoggerFactory.getLogger(AgentConfig.class);
    /** The prompt content for the extractor personality, loaded from the classpath resource. */
    @Value("classpath:prompts/recipe-extractor-agent-personality.md")
    private String recipeExtractorAgentPersonality;
    /** The prompt content for the chat personality, loaded from the classpath resource. */
    @Value("classpath:prompts/recipe-chat-agent-personality.md")
    private String recipeChatAgentPersonality;
    /** The prompt content for the agent user instruction, loaded from the classpath resource. */
    @Value("classpath:prompts/agent-user-instruction.md")
    private String agentUserInstruction;

    /**
     * Bean definition for the ChatClient used in recipe extraction interactions.
     * This method configures the ChatClient with a default system prompt that includes the extractor personality and the recipe schema format, and enables native structured output.
     *
     * @param builder the ChatClient.Builder used to build the ChatClient instance
     * @param converter the RecipeSchemaConverter used to provide the format for the system prompt
     * @return a configured ChatClient instance for recipe extraction interactions
     */
    @Bean
    public ChatClient recipeExtractorAgent(ChatClient.Builder builder, RecipeSchemaMapper converter) {
        log.info("Creating chat client for extractor agent");
        return builder
                .defaultSystem(recipeExtractorAgentPersonality + "\n" + converter.getFormat())
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

    /**
     * Bean definition for the agent user instruction, which provides guidance to users interacting with the agent.
     *
     * @return a String containing the agent user instruction loaded from the classpath resource
     */
    @Bean
    public String agentUserInstruction() {
        log.info("Loading agent user instruction from resource...");
        return agentUserInstruction;
    }

}
