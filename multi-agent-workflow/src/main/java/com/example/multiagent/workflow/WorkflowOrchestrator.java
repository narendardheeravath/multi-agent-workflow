package com.example.multiagent.workflow;

import com.example.multiagent.agent.AgentFactory;
import com.example.multiagent.agent.ReportWriterAgent;
import com.example.multiagent.agent.ResearcherAgent;
import com.example.multiagent.agent.SummarizerAgent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Orchestrates the three-agent pipeline:
 *   ResearcherAgent → SummarizerAgent → ReportWriterAgent
 *
 * Each agent runs sequentially; the output of each feeds the next.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WorkflowOrchestrator {

    private final AgentFactory agentFactory;

    @Value("${llm.provider:openai}")
    private String llmProvider;

    public WorkflowResult execute(String topic) {
        LocalDateTime startedAt = LocalDateTime.now();
        long startMs = System.currentTimeMillis();

        log.info("══════════════════════════════════════════");
        log.info("  Multi-Agent Workflow STARTED");
        log.info("  Topic    : {}", topic);
        log.info("  Provider : {}", llmProvider.toUpperCase());
        log.info("══════════════════════════════════════════");

        // ── Step 1: Research ────────────────────────────────────────────────
        log.info("[1/3] ResearcherAgent — gathering information...");
        ResearcherAgent researcherAgent = agentFactory.createResearcherAgent();
        String research = researcherAgent.research(topic);
        log.info("[1/3] Research complete ({} chars)", research.length());

        // ── Step 2: Summarise ───────────────────────────────────────────────
        log.info("[2/3] SummarizerAgent — condensing research...");
        SummarizerAgent summarizerAgent = agentFactory.createSummarizerAgent();
        String summary = summarizerAgent.summarize(research);
        log.info("[2/3] Summary complete ({} chars)", summary.length());

        // ── Step 3: Write Report ────────────────────────────────────────────
        log.info("[3/3] ReportWriterAgent — writing professional report...");
        ReportWriterAgent reportWriterAgent = agentFactory.createReportWriterAgent();
        String report = reportWriterAgent.writeReport(summary);
        log.info("[3/3] Report complete ({} chars)", report.length());

        long durationMs = System.currentTimeMillis() - startMs;
        log.info("══════════════════════════════════════════");
        log.info("  Multi-Agent Workflow COMPLETED in {}ms", durationMs);
        log.info("══════════════════════════════════════════");

        return WorkflowResult.builder()
                .topic(topic)
                .research(research)
                .summary(summary)
                .report(report)
                .llmProvider(llmProvider)
                .startedAt(startedAt)
                .completedAt(LocalDateTime.now())
                .durationMs(durationMs)
                .build();
    }
}
