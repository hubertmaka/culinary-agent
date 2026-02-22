# 🍳 Culinary Agent

A Spring Boot AI-powered REST API that extracts structured recipe data from any source (URL, image, plain text) and provides a real-time voice-enabled cooking assistant with text-to-speech output.

---

## Table of Contents

- [Overview](#overview)
- [Architecture](#architecture)
- [Tech Stack](#tech-stack)
- [API Reference](#api-reference)
  - [POST /api/v1/recipes/extract](#post-apiv1recipesextract)
  - [POST /api/v1/recipes/stream](#post-apiv1recipesstream)
- [Data Models](#data-models)
  - [Enums](#enums)
  - [Request DTOs](#request-dtos)
  - [Response DTOs](#response-dtos)
- [Configuration](#configuration)
- [Running the Application](#running-the-application)
- [Running with Docker](#running-with-docker)
- [Running Tests](#running-tests)

---

## Overview

Culinary Agent is a two-part AI system:

1. **Recipe Extractor** — Takes raw content from a URL, image, or plain text, strips all noise (blog stories, ads, SEO filler), and returns a fully structured recipe schema including ingredients list, preparation time, and AI estimations for information missing from the original source.

2. **Cooking Chat Assistant** — A focused, real-time conversational assistant that guides the user through cooking a specific recipe. Every response is streamed back as both a text chunk and a synthesised audio stream via ElevenLabs TTS.

---

## Architecture

```
Client
  │
  ├─ POST /extract ──► RecipeProcessorController
  │                         │
  │                         ▼
  │                   GeminiRecipeExtractorService
  │                         │
  │                   RecipeInputStrategy (URL / Image / Text)
  │                         │
  │                   Gemini (Google GenAI)
  │                         │
  │                   RecipeSchemaMapper (BeanOutputConverter)
  │                         │
  │                   RecipeSchemaResponseDto ◄──────────────────────┐
  │                                                                   │
  └─ POST /stream ──► RecipeProcessorController                       │
                           │                                          │
                           ▼                                          │
                     GeminiRecipeChatService ─────── RecipeSchemaDto ─┘
                           │
                     ElevenLabsTTSService
                           │
                     SSE Stream (audio chunks + agent metadata)
```

**Input strategies** follow the Strategy pattern — each source type (`URL`, `IMAGE`, `TEXT`) has a dedicated implementation that pre-processes the content before sending it to the model:

| Strategy | Source | Behaviour |
|---|---|---|
| `UrlRecipeInputStrategy` | `URL` | Fetches HTML via `RestClient`, parses body text with Jsoup |
| `ImageRecipeInputStrategy` | `IMAGE` | Decodes Base64 image and attaches it as a multimodal message |
| `TextRecipeInputStrategy` | `TEXT` | Passes plain text directly as a user message |

---

## Tech Stack

| Layer | Technology |
|---|---|
| Runtime | Java 21 |
| Framework | Spring Boot 3.4.3 |
| AI Orchestration | Spring AI 1.1.2 |
| LLM | Google Gemini (`gemini-3-flash-preview` by default) |
| TTS | ElevenLabs (`eleven_flash_v2_5` by default) |
| HTML Scraping | Jsoup 1.22.1 |
| API Docs | SpringDoc OpenAPI 2.8.5 (Swagger UI at `/swagger-ui.html`) |
| Reactive | Project Reactor (WebFlux) |
| Containerisation | Docker |

---

## API Reference

### POST `/api/v1/recipes/extract`

Extracts a structured recipe from any supported source.

**Request body** — `application/json`:

```json
{
  "content": "https://example.com/my-recipe",
  "contentType": "URL",
  "fileExtension": "JPG",
  "language": "PL"
}
```

**Response** — `200 OK`, `application/json`:

```json
{
  "recipeSchema": {
    "content": "# Spaghetti Bolognese\n\n> ...",
    "ingredients": [
      { "ingredient": "500 g mielonej wołowiny" }
    ],
    "preparationTimeInMinutes": 45,
    "aiEstimations": [
      {
        "additionalIngredients": [
          { "ingredient": "sól do wody makaronowej, do smaku" }
        ],
        "estimatedPreparationTimeMinutes": null
      }
    ]
  },
  "metadata": {
    "inputTokens": 312,
    "outputTokens": 890,
    "totalTokens": 1202,
    "model": "gemini-3-flash-preview"
  }
}
```

---

### POST `/api/v1/recipes/stream`

Sends a message to the cooking assistant and streams the response back as Server-Sent Events. Each SSE stream consists of:

| SSE Event | Content | Description |
|---|---|---|
| `audio` | `{ "audioChunk": "<base64>", "agentResponse": null }` | Raw PCM audio chunks from ElevenLabs, streamed as they arrive |
| `agent_completion` | `{ "audioChunk": null, "agentResponse": { "content": "...", "metadata": {...} } }` | Full text response + Gemini token usage |
| `end` | `{ "audioChunk": null, "agentResponse": { "content": null, "metadata": {...} } }` | TTS model metadata (signals end of stream) |

**Request body** — `application/json`:

```json
{
  "schema": {
    "content": "# Spaghetti Bolognese\n\n...",
    "ingredients": [{ "ingredient": "500 g mielonej wołowiny" }],
    "preparationTimeInMinutes": 45,
    "aiEstimations": []
  },
  "messages": [
    { "role": "USER", "content": "Właśnie podsmażam mięso, co dalej?" }
  ],
  "voice": "VOICE_WOMAN",
  "language": "PL"
}
```

**Response** — `200 OK`, `text/event-stream`

---

## Data Models

### Enums

#### `Language`
| Value | Description |
|---|---|
| `PL` | Polish |
| `EN_US` | English (US) |
| `EN_GB` | English (GB) |
| `DE` | German |
| `FR` | French |
| `SP` | Spanish |

#### `RecipeSource`
| Value | Description |
|---|---|
| `URL` | Web page URL — HTML is fetched and parsed automatically |
| `IMAGE` | Base64-encoded image (JPEG / JPG / PNG) |
| `TEXT` | Raw plain text recipe |

#### `FileExtension`
`JPEG`, `JPG`, `PNG` — required when `contentType` is `IMAGE`, otherwise pass any value.

#### `Voice`
| Value | Description |
|---|---|
| `VOICE_WOMAN` | Female voice (ElevenLabs ID: `hpp4J3VqNfWAUOO0d1Us`) |
| `VOICE_MAN` | Male voice (ElevenLabs ID: `CwhRBWXzGAHq8TQ4Fs17`) |

#### `Role`
`USER`, `ASSISTANT`, `SYSTEM` — used in conversation history messages.

---

### Request DTOs

#### `RecipeDataRequestDto`
| Field | Type | Constraints | Description |
|---|---|---|---|
| `content` | `String` | Required, max 10 000 chars | URL, Base64 image, or plain text |
| `contentType` | `RecipeSource` | Required | Source type |
| `fileExtension` | `FileExtension` | Required | Image format (relevant for `IMAGE` source) |
| `language` | `Language` | Required | Output language for the extracted recipe |

#### `RecipeChatRequestDto`
| Field | Type | Constraints | Description |
|---|---|---|---|
| `schema` | `RecipeSchemaDto` | Required | The recipe the user is currently cooking |
| `messages` | `List<MessageDto>` | Required | Full conversation history |
| `voice` | `Voice` | Required | TTS voice selection |
| `language` | `Language` | Required | Language for the assistant's response |

#### `MessageDto`
| Field | Type | Description |
|---|---|---|
| `role` | `Role` | `USER` or `ASSISTANT` |
| `content` | `String` | Message text, max 10 000 chars |

---

### Response DTOs

#### `RecipeSchemaDto`
| Field | Type | Description |
|---|---|---|
| `content` | `String` | Markdown-formatted recipe body (max 20 000 chars) |
| `ingredients` | `List<IngredientDto>` | Structured ingredient list |
| `preparationTimeInMinutes` | `Integer` | Total prep + cook time from source |
| `aiEstimations` | `List<AIEstimationDto>` | Items absent from source but essential for execution |

#### `AIEstimationDto`
| Field | Type | Description |
|---|---|---|
| `additionalIngredients` | `List<IngredientDto>` | Ingredients missing from source but required |
| `estimatedPreparationTimeMinutes` | `Integer` | Time estimate if source didn't provide one; `null` otherwise |

#### `RecipeSchemaResponseDto`
| Field | Type | Description |
|---|---|---|
| `recipeSchema` | `RecipeSchemaDto` | Extracted recipe data |
| `metadata` | `MetadataDto` | Token usage and model info |

#### `MetadataDto`
| Field | Type | Description |
|---|---|---|
| `inputTokens` | `Integer` | Prompt tokens consumed |
| `outputTokens` | `Integer` | Completion tokens generated |
| `totalTokens` | `Integer` | Total tokens |
| `model` | `String` | Model identifier |

---

## Configuration

All sensitive values are injected via environment variables. Set them before running the application:

| Environment Variable | Required | Default | Description |
|---|---|---|---|
| `GOOGLE_GENAI_API_KEY` | ✅ | — | Google AI Studio API key |
| `GEMINI_GENAI_MODEL` | ❌ | `gemini-3-flash-preview` | Gemini model identifier |
| `ELEVENLABS_API_KEY` | ✅ | — | ElevenLabs API key |
| `ELEVENLABS_TTS_MODEL_ID` | ❌ | `eleven_flash_v2_5` | ElevenLabs TTS model identifier |

---

## Running the Application

```bash
export GOOGLE_GENAI_API_KEY=your_google_key
export ELEVENLABS_API_KEY=your_elevenlabs_key

./mvnw spring-boot:run
```

The application starts on port **8080** by default.

Swagger UI is available at: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

---

## Running with Docker

```bash
docker build -t culinary-agent .

docker run -p 8080:8080 \
  -e GOOGLE_GENAI_API_KEY=your_google_key \
  -e ELEVENLABS_API_KEY=your_elevenlabs_key \
  culinary-agent
```

---

## Running Tests

```bash
./mvnw test
```

Test reports are generated in `target/surefire-reports/`.
