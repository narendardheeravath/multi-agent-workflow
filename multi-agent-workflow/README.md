# 🤖 Multi-Agent Workflow POC

A Spring Boot + LangChain4j application that chains three AI agents in sequence:

```
User Topic
   │
   ▼
┌─────────────────┐
│ ResearcherAgent │  ── gathers comprehensive information on the topic
└────────┬────────┘
         │ research output
         ▼
┌──────────────────┐
│ SummarizerAgent  │  ── condenses the research into key points
└────────┬─────────┘
         │ summary
         ▼
┌───────────────────┐
│ ReportWriterAgent │  ── writes a polished executive report
└────────┬──────────┘
         │
         ▼
   Final Report (JSON response)
```

---

## 🛠️ Tech Stack

| Layer          | Technology                          |
|----------------|-------------------------------------|
| Framework      | Spring Boot 3.3.5                   |
| AI Integration | LangChain4j 0.36.2 (AiServices)     |
| LLM Options    | OpenAI GPT-4o **or** Ollama (local) |
| API Docs       | SpringDoc OpenAPI / Swagger UI      |
| Build          | Maven + Java 21                     |

---

## ⚙️ Prerequisites

- Java 21+
- Maven 3.9+
- **One of the following LLM providers:**
  - **OpenAI**: An API key from https://platform.openai.com
  - **Ollama**: Installed locally at http://localhost:11434 with a model pulled

### Install & start Ollama (if using local LLM)
```bash
# Install Ollama: https://ollama.com/download
ollama pull llama3.2          # or mistral, qwen2.5, etc.
ollama serve                  # starts on http://localhost:11434
```

---

## 🚀 Running the Application

### Option A — OpenAI GPT-4o (default)

```bash
# Set your API key
export OPENAI_API_KEY=sk-...

# Run
mvn spring-boot:run
```

Or with the env var inline:
```bash
OPENAI_API_KEY=sk-... mvn spring-boot:run
```

### Option B — Ollama (local, no API key needed)

```bash
# Switch provider via env var
LLM_PROVIDER=ollama mvn spring-boot:run

# Or override model:
LLM_PROVIDER=ollama OLLAMA_MODEL=mistral mvn spring-boot:run
```

### Option C — Edit application.yml directly

```yaml
llm:
  provider: ollama   # change 'openai' → 'ollama'
```

---

## 🔀 Switching LLM Provider

| Method              | Command / Setting                              |
|---------------------|------------------------------------------------|
| Environment variable | `LLM_PROVIDER=ollama`                         |
| JVM argument        | `-Dllm.provider=ollama`                        |
| application.yml     | `llm.provider: ollama`                         |
| Maven property      | `mvn spring-boot:run -Dspring-boot.run.arguments="--llm.provider=ollama"` |

### Supported models

| Provider | Default Model | Other Options                    |
|----------|---------------|----------------------------------|
| OpenAI   | `gpt-4o`      | `gpt-4o-mini`, `gpt-3.5-turbo`   |
| Ollama   | `llama3.2`    | `mistral`, `qwen2.5`, `gemma3`   |

Change model via env var: `OPENAI_MODEL=gpt-4o-mini` or `OLLAMA_MODEL=mistral`

---

## 📡 API Reference

### POST /api/workflow/run

Run the full Researcher → Summarizer → Report Writer pipeline.

**Request:**
```json
{
  "topic": "The impact of Artificial Intelligence on modern software development"
}
```

**Response:**
```json
{
  "topic": "The impact of AI on modern software development",
  "research": "... comprehensive research from Agent 1 ...",
  "summary":  "... condensed key points from Agent 2 ...",
  "report":   "# Executive Summary\n...",
  "llmProvider": "openai",
  "startedAt":   "2024-01-15T10:30:00",
  "completedAt": "2024-01-15T10:30:45",
  "durationMs":  45231,
  "status": "SUCCESS"
}
```

### GET /api/workflow/health
Returns `200 OK` if the service is running.

### Swagger UI
Open **http://localhost:8080/swagger-ui.html** for interactive API docs.

---

## 🧪 Testing

```bash
# Run tests
mvn test

# Quick curl test (OpenAI)
curl -X POST http://localhost:8080/api/workflow/run \
  -H "Content-Type: application/json" \
  -d '{"topic": "Quantum computing fundamentals"}'
```

---

## 📁 Project Structure

```
src/main/java/com/example/multiagent/
├── MultiAgentWorkflowApplication.java   # Entry point
├── config/
│   └── LlmConfig.java                   # OpenAI / Ollama bean (flag-driven)
├── agent/
│   ├── ResearcherAgent.java             # Agent 1: interface + @SystemMessage
│   ├── SummarizerAgent.java             # Agent 2: interface + @SystemMessage
│   ├── ReportWriterAgent.java           # Agent 3: interface + @SystemMessage
│   └── AgentFactory.java                # AiServices wiring
├── workflow/
│   ├── WorkflowOrchestrator.java        # Chains the 3 agents
│   └── WorkflowResult.java             # Internal result model
├── controller/
│   └── WorkflowController.java          # REST endpoints
├── model/
│   ├── WorkflowRequest.java             # API request DTO
│   └── WorkflowResponse.java            # API response DTO
└── exception/
    └── GlobalExceptionHandler.java      # Error handling
```

---

## 🔧 Configuration Reference

```yaml
llm:
  provider: openai           # 'openai' | 'ollama'  — env: LLM_PROVIDER

  openai:
    api-key: sk-...          # env: OPENAI_API_KEY
    model: gpt-4o            # env: OPENAI_MODEL
    temperature: 0.7
    timeout-seconds: 60

  ollama:
    base-url: http://localhost:11434   # env: OLLAMA_BASE_URL
    model: llama3.2                    # env: OLLAMA_MODEL
    temperature: 0.7
    timeout-seconds: 120
```

---

## 🏗️ How It Works

1. **POST /api/workflow/run** receives a topic.
2. `WorkflowOrchestrator` creates three isolated agents via `AgentFactory`.
3. Each agent is a LangChain4j `AiService` backed by the same `ChatLanguageModel` bean.
4. The pipeline runs sequentially — each agent receives the previous agent's output.
5. The final JSON response contains all three outputs plus timing metadata.

LLM selection happens once at Spring startup via `LlmConfig.chatLanguageModel()` — a single
`@Bean` that checks `llm.provider` and constructs either `OpenAiChatModel` or `OllamaChatModel`.
No code change is needed to switch providers.
