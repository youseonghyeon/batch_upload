package com.example.excel.service.validator.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SmsValidatorParam extends MessageValidatorParam {

    private final String messageName;
    private final String messageContent;

}
