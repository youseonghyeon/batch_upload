package com.example.excel.service.validator;

import com.example.excel.service.validator.dto.MessageValidatorParam;
import com.example.excel.service.validator.dto.SmsValidatorParam;
import com.example.excel.service.validator.dto.ValidatorBindingReuslt;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SmsValidator implements MessageValidator {

    @Override
    public boolean supports(MessageType messageType) {
        return MessageType.SMS.equals(messageType);
    }

    @Override
    public List<ValidatorBindingReuslt> validate(MessageValidatorParam param) {
        if (!(param instanceof SmsValidatorParam smsValidatorParam)) {
            throw new IllegalArgumentException("param is not instance of SmsValidatorParam");
        }

        return new ArrayList<>();
    }

    private List<ValidatorBindingReuslt> validateFieldLengths() {
        return new ArrayList<>();
    }

    private List<ValidatorBindingReuslt> validateVariableTexts() {
        return new ArrayList<>();
    }


}
