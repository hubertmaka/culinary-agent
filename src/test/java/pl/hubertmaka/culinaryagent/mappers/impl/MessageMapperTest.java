package pl.hubertmaka.culinaryagent.mappers.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import pl.hubertmaka.culinaryagent.domain.dtos.MessageDto;
import pl.hubertmaka.culinaryagent.domain.enums.Role;
import pl.hubertmaka.culinaryagent.exceptions.UnsupportedRoleException;

import static org.junit.jupiter.api.Assertions.*;

class MessageMapperTest {

    private MessageMapper messageMapper;

    @BeforeEach
    void setUp() {
        messageMapper = new MessageMapper();
    }

    @Test
    @DisplayName("Test if MessageMapper is initialized correctly")
    void whenInitialized_thenNotNull() {
        // Then
        assertNotNull(messageMapper);
    }

    @Test
    @DisplayName("Test if MessageMapper maps a Message to MessageDto correctly for user")
    void whenMapToUserMessage_thenReturnMessageDto() {
        // Given
        var userMessage = new UserMessage("Hello, this is a test message.");

        // When
        var messageDto = messageMapper.mapTo(userMessage);

        // Then
        assertNotNull(messageDto);
        assertEquals("Hello, this is a test message.", messageDto.content());
        assertEquals(Role.USER, messageDto.role());
    }

    @Test
    @DisplayName("Test if MessageMapper maps a MessageDto to Message correctly for user")
    void whenMapFromUserMessage_thenReturnMessage() {
        // Given
        var messageDto = new MessageDto(Role.USER, "Hello, this is a test message.");

        // When
        var message = messageMapper.mapFrom(messageDto);

        // Then
        assertNotNull(message);
        assertInstanceOf(UserMessage.class, message);
        assertEquals("Hello, this is a test message.", message.getText());
    }

    @Test
    @DisplayName("Test if MessageMapper throws UnsupportedRoleException for assistant role")
    void whenMapToAssistantMessage_thenReturnMessageDto() {
        // Given
        var assistantMessage = new AssistantMessage("Hello, this is a test message.");

        // When
        var messageDto = messageMapper.mapTo(assistantMessage);

        // Then
        assertNotNull(messageDto);
        assertEquals("Hello, this is a test message.", messageDto.content());
        assertEquals(Role.ASSISTANT, messageDto.role());
    }

    @Test
    @DisplayName("Test if MessageMapper maps a MessageDto to Message correctly for assistant")
    void whenMapFromAssistantMessage_thenReturnMessage() {
        // Given
        var messageDto = new MessageDto(Role.ASSISTANT, "Hello, this is a test message.");

        // When
        var message = messageMapper.mapFrom(messageDto);

        // Then
        assertNotNull(message);
        assertInstanceOf(AssistantMessage.class, message);
        assertEquals("Hello, this is a test message.", message.getText());
    }

    @Test
    @DisplayName("Test if MessageMapper throws NullPointerException for unsupported null role")
    void whenMapFromUnsupportedRole_thenThrowException() {
        // Given
        var messageDto = new MessageDto(null, "Hello, this is a test message.");

        // When & Then
        assertThrows(NullPointerException.class, () -> messageMapper.mapFrom(messageDto));
    }

    @Test
    @DisplayName("Test if MessageMapper throws UnsupportedRoleException for unsupported role")
    void whenMapFromUnsupportedRole_thenThrowUnsupportedRoleException() {
        // Given
        var messageDto = new MessageDto(Role.SYSTEM, "Hello, this is a test message.");

        // When & Then
        assertThrows(UnsupportedRoleException.class, () -> messageMapper.mapFrom(messageDto));
    }

    @Test
    @DisplayName("Test if MessageMapper throws UnsupportedRoleException for unsupported message type")
    void whenMapToUnsupportedMessage_thenThrowException() {
        // Given
        var unsupportedMessage = new SystemMessage("Hello, this is a test message.");

        // When & Then
        assertThrows(UnsupportedRoleException.class, () -> messageMapper.mapTo(unsupportedMessage));
    }
}