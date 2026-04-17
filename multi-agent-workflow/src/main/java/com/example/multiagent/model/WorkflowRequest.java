package com.example.multiagent.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Request to trigger the multi-agent workflow")
public class WorkflowRequest {

    @NotBlank(message = "Topic is required")
    @Size(min = 3, max = 500, message = "Topic must be between 3 and 500 characters")
    @Schema(
        description = "The topic to research, summarise, and report on",
        example = "The impact of Artificial Intelligence on modern software development"
    )
    private String topic;
}
