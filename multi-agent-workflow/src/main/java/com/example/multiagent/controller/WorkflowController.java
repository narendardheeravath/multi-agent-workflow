package com.example.multiagent.controller;

import com.example.multiagent.model.WorkflowRequest;
import com.example.multiagent.model.WorkflowResponse;
import com.example.multiagent.workflow.WorkflowOrchestrator;
import com.example.multiagent.workflow.WorkflowResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/workflow")
@RequiredArgsConstructor
@Tag(name = "Multi-Agent Workflow", description = "Trigger and monitor the Researcher → Summarizer → Report Writer pipeline")
public class WorkflowController {

    private final WorkflowOrchestrator orchestrator;

    @PostMapping("/run")
    @Operation(
        summary = "Run multi-agent workflow",
        description = "Executes the full Researcher → Summarizer → Report Writer agent chain for the given topic"
    )
    public ResponseEntity<WorkflowResponse> runWorkflow(@Valid @RequestBody WorkflowRequest request) {
        log.info("Workflow request received. Topic: '{}'", request.getTopic());

        WorkflowResult result = orchestrator.execute(request.getTopic());

        return ResponseEntity.ok(WorkflowResponse.builder()
                .topic(result.getTopic())
                .research(result.getResearch())
                .summary(result.getSummary())
                .report(result.getReport())
                .llmProvider(result.getLlmProvider())
                .startedAt(result.getStartedAt())
                .completedAt(result.getCompletedAt())
                .durationMs(result.getDurationMs())
                .status("SUCCESS")
                .build());
    }

    @GetMapping("/health")
    @Operation(summary = "Health check", description = "Verify the workflow service is up")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Multi-Agent Workflow Service is running");
    }
}
