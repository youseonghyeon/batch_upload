package com.example.excel.service.validator;

import com.example.excel.service.validator.dto.MessageValidatorParam;
import com.example.excel.service.validator.dto.RcsMmsValidatorParam;
import com.example.excel.service.validator.dto.ValidatorBindingReuslt;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class RcsMmsValidator implements MessageValidator {

    @Override
    public boolean supports(MessageType messageType) {
        return MessageType.RCS.equals(messageType);
    }

    @Override
    public List<ValidatorBindingReuslt> validate(MessageValidatorParam param) {
        if (!(param instanceof RcsMmsValidatorParam rcsMmsValidatorParam)) {
            throw new IllegalArgumentException("param is not instance of RcsMmsValidatorParam");
        }

        List<ValidatorBindingReuslt> bindingResult = new ArrayList<>();
        Random random = new Random();
        int i = random.nextInt() % 2;
        if (i == 1) {
            bindingResult.add(new ValidatorBindingReuslt("web reject messages", "batch reject messages"));
        }
        if (rcsMmsValidatorParam.getMessageContent() == null) {
            bindingResult.add(new ValidatorBindingReuslt("rcsMms", "rcsMms is required"));
        }

        return bindingResult;
    }
}
