package com.example.multiagent.workflow;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class WorkflowResult {
    private String topic;
    private String research;
    private String summary;
    private String report;
    private String llmProvider;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    private long durationMs;
}
