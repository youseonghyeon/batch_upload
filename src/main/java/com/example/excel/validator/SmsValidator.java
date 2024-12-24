package com.example.excel.validator;

import com.example.excel.validator.dto.MessageValidatorParam;
import com.example.excel.validator.dto.SmsValidatorParam;
import com.example.excel.validator.dto.ValidatorBindingResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Validator;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SmsValidator implements MessageValidator {

    private final Validator validator;

    @Override
    public boolean supports(MessageType messageType) {
        return MessageType.SMS.equals(messageType);
    }

    @Override
    public List<ValidatorBindingResult> validate(MessageValidatorParam param) {
        if (!(param instanceof SmsValidatorParam smsValidatorParam)) {
            throw new IllegalArgumentException("param is not instance of SmsValidatorParam");
        }

        return new ArrayList<>();
    }

    private List<ValidatorBindingResult> validateFieldLengths() {
        return new ArrayList<>();
    }

    private List<ValidatorBindingResult> validateVariableTexts() {
        return new ArrayList<>();
    }


}
