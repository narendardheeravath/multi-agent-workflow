package com.example.multiagent;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
    "llm.provider=openai",
    "llm.openai.api-key=test-key-for-context-load"
})
class MultiAgentWorkflowApplicationTests {

    @Test
    void contextLoads() {
        // Verifies the Spring application context starts correctly
    }
}
