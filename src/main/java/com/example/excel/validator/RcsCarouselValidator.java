package com.example.excel.validator;

import com.example.excel.validator.dto.MessageValidatorParam;
import com.example.excel.validator.dto.RcsCarouselValidatorParam;
import com.example.excel.validator.dto.ValidatorBindingResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Validator;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RcsCarouselValidator implements MessageValidator {

    private final Validator validator;

    @Override
    public boolean supports(MessageType messageType) {
        return MessageType.RCS_CAROUSEL.equals(messageType);
    }

    @Override
    public List<ValidatorBindingResult> validate(MessageValidatorParam param) {
        if (!(param instanceof RcsCarouselValidatorParam rcsCarouselValidatorParam)) {
            throw new IllegalArgumentException("param is not instance of RcsCarouselValidatorParam");
        }

        return new ArrayList<>();
    }
}
