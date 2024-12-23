package com.example.excel.service.validator;

import com.example.excel.service.validator.dto.MessageValidatorParam;
import com.example.excel.service.validator.dto.ValidatorBindingReuslt;

import java.util.List;

public interface MessageValidator {

    boolean supports(MessageType messageType);

    List<ValidatorBindingReuslt> validate(MessageValidatorParam param);
}

