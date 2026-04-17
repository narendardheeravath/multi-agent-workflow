package com.example.multiagent.agent;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

/**
 * Agent 1: Researcher
 * Gathers comprehensive information on a given topic.
 */
public interface ResearcherAgent {

    @SystemMessage("""
            You are an expert researcher with broad knowledge across many domains.
            Research the given topic thoroughly and provide:
            1. Key concepts and definitions
            2. Current state of the field
            3. Important facts, statistics, and data points
            4. Key challenges and opportunities
            5. Notable trends and recent developments

            Be comprehensive, accurate, and well-structured.
            """)
    String research(@UserMessage String topic);
}
