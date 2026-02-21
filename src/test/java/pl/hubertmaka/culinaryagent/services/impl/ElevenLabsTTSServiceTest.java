package pl.hubertmaka.culinaryagent.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.audio.tts.Speech;
import org.springframework.ai.audio.tts.TextToSpeechModel;
import org.springframework.ai.audio.tts.TextToSpeechPrompt;
import org.springframework.ai.audio.tts.TextToSpeechResponse;
import org.springframework.test.util.ReflectionTestUtils;
import pl.hubertmaka.culinaryagent.domain.dtos.RecipeChatResponseChunkDto;
import pl.hubertmaka.culinaryagent.domain.enums.Voice;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ElevenLabsTTSServiceTest {

    @Mock
    private TextToSpeechModel textToSpeechModel;

    private ElevenLabsTTSService elevenLabsTTSService;

    @BeforeEach
    void setUp() {
        elevenLabsTTSService = new ElevenLabsTTSService(textToSpeechModel);
    }

    @Test
    @DisplayName("Test if ElevenLabsTTSService is initialized correctly")
    void whenInitialized_thenNotNull() {
        // Then
        assertNotNull(elevenLabsTTSService);
    }

    @Test
    @DisplayName("Test if getModel returns the correct model identifier")
    void whenGetModel_thenReturnCurrentModelId() {
        // Given
        ReflectionTestUtils.setField(elevenLabsTTSService, "modelId", "eleven_flash_v2_5");

        // When
        String modelId = elevenLabsTTSService.getModel();

        // Then
        assertEquals("eleven_flash_v2_5", modelId);
    }

    @Test
    @DisplayName("Test if stream returns a Flux of RecipeChatResponseChunkDto with audio chunk and null agent response")
    void whenStream_thenReturnFluxOfRecipeChatResponseChunkDto() {
        // Given
        String text = "Hello, this is a test.";
        Voice voice = Voice.VOICE_WOMAN;
        Speech speech = mock(Speech.class);
        TextToSpeechResponse response = new TextToSpeechResponse(List.of(speech));

        when(textToSpeechModel.stream(any(TextToSpeechPrompt.class))).thenReturn(Flux.just(response));

        // When
        Flux<RecipeChatResponseChunkDto> result = elevenLabsTTSService.stream(text, voice);

        // Then
        assertNotNull(result);
        StepVerifier.create(result)
            .assertNext(chunk -> {
                assertEquals(speech, chunk.audioChunk());
                assertNull(chunk.agentResponse());
            })
            .verifyComplete();
    }

    @Test
    @DisplayName("Test if stream uses the correct voice id when calling the model")
    void whenStreamWithVoiceWoman_thenUseCorrectVoiceId() {
        // Given
        String text = "Test voice woman.";
        Voice voice = Voice.VOICE_WOMAN;
        Speech speech = mock(Speech.class);
        TextToSpeechResponse response = new TextToSpeechResponse(List.of(speech));

        when(textToSpeechModel.stream(any(TextToSpeechPrompt.class))).thenReturn(Flux.just(response));

        // When
        elevenLabsTTSService.stream(text, voice).blockFirst();

        // Then
        verify(textToSpeechModel, times(1)).stream(any(TextToSpeechPrompt.class));
    }

    @Test
    @DisplayName("Test if stream uses the correct voice id for VOICE_MAN")
    void whenStreamWithVoiceMan_thenUseCorrectVoiceId() {
        // Given
        String text = "Test voice man.";
        Voice voice = Voice.VOICE_MAN;
        Speech speech = mock(Speech.class);
        TextToSpeechResponse response = new TextToSpeechResponse(List.of(speech));

        when(textToSpeechModel.stream(any(TextToSpeechPrompt.class))).thenReturn(Flux.just(response));

        // When
        elevenLabsTTSService.stream(text, voice).blockFirst();

        // Then
        verify(textToSpeechModel, times(1)).stream(any(TextToSpeechPrompt.class));
    }

    @Test
    @DisplayName("Test if stream emits multiple chunks when model returns multiple responses")
    void whenStreamWithMultipleResponses_thenEmitMultipleChunks() {
        // Given
        String text = "Multi-chunk test.";
        Voice voice = Voice.VOICE_WOMAN;
        Speech speech1 = mock(Speech.class);
        Speech speech2 = mock(Speech.class);
        TextToSpeechResponse response1 = new TextToSpeechResponse(List.of(speech1));
        TextToSpeechResponse response2 = new TextToSpeechResponse(List.of(speech2));

        when(textToSpeechModel.stream(any(TextToSpeechPrompt.class))).thenReturn(Flux.just(response1, response2));

        // When
        Flux<RecipeChatResponseChunkDto> result = elevenLabsTTSService.stream(text, voice);

        // Then
        StepVerifier.create(result)
            .assertNext(chunk -> {
                assertEquals(speech1, chunk.audioChunk());
                assertNull(chunk.agentResponse());
            })
            .assertNext(chunk -> {
                assertEquals(speech2, chunk.audioChunk());
                assertNull(chunk.agentResponse());
            })
            .verifyComplete();
    }

    @Test
    @DisplayName("Test if stream returns empty Flux when model returns empty Flux")
    void whenStreamWithEmptyResponse_thenReturnEmptyFlux() {
        // Given
        String text = "Empty response test.";
        Voice voice = Voice.VOICE_WOMAN;

        when(textToSpeechModel.stream(any(TextToSpeechPrompt.class))).thenReturn(Flux.empty());

        // When
        Flux<RecipeChatResponseChunkDto> result = elevenLabsTTSService.stream(text, voice);

        // Then
        StepVerifier.create(result)
            .verifyComplete();
    }
}