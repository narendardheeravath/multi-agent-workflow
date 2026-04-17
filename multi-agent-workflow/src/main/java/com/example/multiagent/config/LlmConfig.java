package com.example.multiagent.config;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Slf4j
@Configuration
public class LlmConfig {

    @Value("${llm.provider:openai}")
    private String provider;

    @Value("${llm.openai.api-key:}")
    private String openAiApiKey;

    @Value("${llm.openai.model:gpt-4o}")
    private String openAiModel;

    @Value("${llm.openai.temperature:0.7}")
    private double openAiTemperature;

    @Value("${llm.openai.timeout-seconds:60}")
    private int openAiTimeoutSeconds;

    @Value("${llm.ollama.base-url:http://localhost:11434}")
    private String ollamaBaseUrl;

    @Value("${llm.ollama.model:llama3.2}")
    private String ollamaModel;

    @Value("${llm.ollama.temperature:0.7}")
    private double ollamaTemperature;

    @Value("${llm.ollama.timeout-seconds:120}")
    private int ollamaTimeoutSeconds;

    @PostConstruct
    public void validate() {
        if ("openai".equalsIgnoreCase(provider) && (openAiApiKey == null || openAiApiKey.isBlank())) {
            throw new IllegalStateException(
                "OpenAI API key is required when llm.provider=openai. " +
                "Set OPENAI_API_KEY env var or llm.openai.api-key in application.yml."
            );
        }
        log.info("LLM provider selected: {}", provider.toUpperCase());
    }

    /**
     * Single ChatLanguageModel bean — resolved at startup based on 'llm.provider'.
     * Switch provider without code changes: set LLM_PROVIDER=ollama (env var).
     */
    @Bean
    public ChatLanguageModel chatLanguageModel() {
        if ("ollama".equalsIgnoreCase(provider)) {
            log.info("Initialising Ollama model '{}' at {}", ollamaModel, ollamaBaseUrl);
            return OllamaChatModel.builder()
                    .baseUrl(ollamaBaseUrl)
                    .modelName(ollamaModel)
                    .temperature(ollamaTemperature)
                    .timeout(Duration.ofSeconds(ollamaTimeoutSeconds))
                    .build();
        }

        log.info("Initialising OpenAI model '{}'", openAiModel);
        return OpenAiChatModel.builder()
                .apiKey(openAiApiKey)
                .modelName(openAiModel)
                .temperature(openAiTemperature)
                .timeout(Duration.ofSeconds(openAiTimeoutSeconds))
                .build();
    }
}
