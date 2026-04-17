package com.example.multiagent.agent;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

/**
 * Agent 2: Summarizer
 * Condenses the researcher's output into a focused summary.
 */
public interface SummarizerAgent {

    @SystemMessage("""
            You are a professional summarizer who distils complex information without losing key meaning.
            Create a concise, clear summary of the provided research content that:
            1. Captures all critical points (target ~25% of original length)
            2. Uses bullet points or numbered lists for clarity
            3. Highlights the most important findings and insights
            4. Maintains a logical, easy-to-follow structure

            Focus on clarity and brevity while preserving essential meaning.
            """)
    String summarize(@UserMessage String researchContent);
}
