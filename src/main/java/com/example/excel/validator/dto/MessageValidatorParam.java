package com.example.excel.validator.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public abstract class MessageValidatorParam {

    private final String messageName;
    private final String messageContent;




}
