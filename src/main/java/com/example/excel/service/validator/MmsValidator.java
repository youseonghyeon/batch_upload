package com.example.excel.service.validator;

import com.example.excel.service.validator.dto.MessageValidatorParam;
import com.example.excel.service.validator.dto.MmsValidatorParam;
import com.example.excel.service.validator.dto.ValidatorBindingReuslt;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MmsValidator implements MessageValidator {

    @Override
    public boolean supports(MessageType messageType) {
        return MessageType.MMS.equals(messageType);
    }

    @Override
    public List<ValidatorBindingReuslt> validate(MessageValidatorParam param) {
        if (!(param instanceof MmsValidatorParam mmsValidatorParam)) {
            throw new IllegalArgumentException("param is not instance of MmsValidatorParam");
        }


        return new ArrayList<>();
    }
}
