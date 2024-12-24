package com.example.excel.validator.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MmsValidatorParam extends MessageValidatorParam {

    private final String messageName;
    private final String messageContent;


}
