package com.example.excel.validator;

import com.example.excel.validator.dto.MessageValidatorParam;
import com.example.excel.validator.dto.RcsMmsValidatorParam;
import com.example.excel.validator.dto.ValidatorBindingResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class RcsMmsValidator implements MessageValidator {

    private final Validator validator;

    @Override
    public boolean supports(MessageType messageType) {
        return MessageType.RCS.equals(messageType);
    }

    @Override
    public List<ValidatorBindingResult> validate(MessageValidatorParam param) {
        if (!(param instanceof RcsMmsValidatorParam rcsMmsValidatorParam)) {
            throw new IllegalArgumentException("param is not instance of RcsMmsValidatorParam");
        }

        List<ValidatorBindingResult> bindingResult = new ArrayList<>();
        Random random = new Random();
        int i = random.nextInt() % 2;
        if (i == 1) {
            bindingResult.add(new ValidatorBindingResult("web reject messages", "batch reject messages"));
        }
        if (rcsMmsValidatorParam.getMessageContent() == null) {
            bindingResult.add(new ValidatorBindingResult("rcsMms", "rcsMms is required"));
        }

        return bindingResult;
    }
}
