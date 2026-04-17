package com.example.multiagent.agent;

import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.service.AiServices;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Factory that wires LangChain4j AiService instances to the configured ChatLanguageModel.
 * Each agent gets its own isolated chat memory window.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AgentFactory {

    private final ChatLanguageModel chatLanguageModel;

    public ResearcherAgent createResearcherAgent() {
        log.debug("Creating ResearcherAgent");
        return AiServices.builder(ResearcherAgent.class)
                .chatLanguageModel(chatLanguageModel)
                .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
                .build();
    }

    public SummarizerAgent createSummarizerAgent() {
        log.debug("Creating SummarizerAgent");
        return AiServices.builder(SummarizerAgent.class)
                .chatLanguageModel(chatLanguageModel)
                .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
                .build();
    }

    public ReportWriterAgent createReportWriterAgent() {
        log.debug("Creating ReportWriterAgent");
        return AiServices.builder(ReportWriterAgent.class)
                .chatLanguageModel(chatLanguageModel)
                .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
                .build();
    }
}
