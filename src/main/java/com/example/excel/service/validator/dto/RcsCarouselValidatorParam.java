package com.example.excel.service.validator.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RcsCarouselValidatorParam extends MessageValidatorParam {

    private final String messageName;
    private final String messageContent;

}