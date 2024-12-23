package com.example.excel.service.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ValidatorFactory {

    private final List<MessageValidator> messageValidatorList;

    public MessageValidator getMessageValidator(MessageType messageType) {
        return messageValidatorList.stream()
                .filter(validator -> validator.supports(messageType))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Validator not found for message type: " + messageType));
    }

}
