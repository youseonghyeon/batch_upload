package com.example.excel.validator;

import com.example.excel.validator.dto.MessageValidatorParam;
import com.example.excel.validator.dto.ValidatorBindingResult;

import java.util.List;

public interface MessageValidator {

    boolean supports(MessageType messageType);

    List<ValidatorBindingResult> validate(MessageValidatorParam param);
}

