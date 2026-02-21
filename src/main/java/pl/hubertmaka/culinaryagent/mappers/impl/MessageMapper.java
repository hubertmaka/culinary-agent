package pl.hubertmaka.culinaryagent.mappers.impl;

import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.stereotype.Component;
import pl.hubertmaka.culinaryagent.domain.dtos.MessageDto;
import pl.hubertmaka.culinaryagent.domain.enums.Role;
import pl.hubertmaka.culinaryagent.exceptions.UnsupportedRoleException;
import pl.hubertmaka.culinaryagent.mappers.Mapper;

import java.util.Optional;

/**
 * Mapper for converting between Message and MessageDto objects.
 */
@Component
public class MessageMapper implements Mapper<Message, MessageDto> {
    /**
     * Maps a MessageDto to a Message object based on the role.
     *
     * @param source the MessageDto to be mapped
     * @return the corresponding Message object
     * @throws UnsupportedRoleException if the role in the MessageDto is unsupported
     */
    @Override
    public Message mapFrom(MessageDto source) {
        switch (source.role()) {
            case USER -> { return UserMessage.builder().text(source.content()).build(); }
            case ASSISTANT -> { return AssistantMessage.builder().content(source.content()).build(); }
            default -> throw new UnsupportedRoleException("Unsupported role");
        }
    }

    /**
     * Maps a Message object to a MessageDto based on the instance type.
     *
     * @param target the Message to be mapped
     * @return the corresponding MessageDto object
     * @throws UnsupportedRoleException if the Message type is unsupported
     */
    @Override
    public MessageDto mapTo(Message target) {
        Optional.ofNullable(target)
                .orElseThrow(() -> new IllegalArgumentException("Message cannot be null"));
        return switch (target) {
            case UserMessage userMessage -> new MessageDto(Role.USER, userMessage.getText());
            case AssistantMessage assistantMessage -> new MessageDto(Role.ASSISTANT, assistantMessage.getText());
            default -> throw new UnsupportedRoleException("Unsupported message type");
        };
    }
}

