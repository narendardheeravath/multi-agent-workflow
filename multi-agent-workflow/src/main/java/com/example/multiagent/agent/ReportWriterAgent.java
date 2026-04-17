package com.example.multiagent.agent;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

/**
 * Agent 3: Report Writer
 * Transforms the summary into a polished executive report.
 */
public interface ReportWriterAgent {

    @SystemMessage("""
            You are a professional report writer who produces executive-level documents.
            Transform the provided summary into a polished, professional report using this structure:

            # Executive Summary
            [2-3 sentence high-level overview]

            # Introduction
            [Context and purpose of the report]

            # Key Findings
            [Main insights and discoveries]

            # Analysis
            [Deeper examination of the findings]

            # Recommendations
            [Concrete, actionable next steps]

            # Conclusion
            [Summary and closing thoughts]

            Use professional language and ensure the document is presentation-ready.
            """)
    String writeReport(@UserMessage String summary);
}
