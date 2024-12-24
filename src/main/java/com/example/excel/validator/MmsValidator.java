package com.example.excel.validator;

import com.example.excel.validator.dto.MessageValidatorParam;
import com.example.excel.validator.dto.MmsValidatorParam;
import com.example.excel.validator.dto.ValidatorBindingResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Validator;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MmsValidator implements MessageValidator {

    private final Validator validator;

    @Override
    public boolean supports(MessageType messageType) {
        return MessageType.MMS.equals(messageType);
    }

    @Override
    public List<ValidatorBindingResult> validate(MessageValidatorParam param) {
        if (!(param instanceof MmsValidatorParam mmsValidatorParam)) {
            throw new IllegalArgumentException("param is not instance of MmsValidatorParam");
        }
        isEmptyOrInvalidLength(param);


        return new ArrayList<>();
    }

    private void isEmptyOrInvalidLength(MessageValidatorParam param) {
        List<ValidatorBindingResult> bindingResults = new ArrayList<>();
        String messageName = param.getMessageName();
        if (outBound(messageName, 10, 600)) {
            bindingResults.add(new ValidatorBindingResult("messageName", "messageName is required and must be between 10 and 600 characters"));
        }
        String messageContent = param.getMessageContent();
        if (outBound(messageName, 10, 600)) {
            bindingResults.add(new ValidatorBindingResult("messageContent", "messageContent is required and must be between 10 and 600 characters"));
        }
    }

    private boolean outBound(String text, int min, int max) {
        if (text == null) {
            return false;
        }
        return text.length() < min
                || text.length() > max;
    }
}
