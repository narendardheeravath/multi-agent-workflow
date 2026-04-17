package com.example.multiagent.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@Schema(description = "Multi-agent workflow response containing all three agent outputs")
public class WorkflowResponse {

    @Schema(description = "The original topic")
    private String topic;

    @Schema(description = "Raw research from the Researcher Agent")
    private String research;

    @Schema(description = "Condensed summary from the Summarizer Agent")
    private String summary;

    @Schema(description = "Final professional report from the Report Writer Agent")
    private String report;

    @Schema(description = "LLM provider used: openai | ollama")
    private String llmProvider;

    @Schema(description = "Workflow start timestamp")
    private LocalDateTime startedAt;

    @Schema(description = "Workflow completion timestamp")
    private LocalDateTime completedAt;

    @Schema(description = "Total workflow duration in milliseconds")
    private long durationMs;

    @Schema(description = "Workflow status")
    private String status;
}
