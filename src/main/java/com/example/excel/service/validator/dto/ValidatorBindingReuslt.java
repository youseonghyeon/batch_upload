package com.example.excel.service.validator.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ValidatorBindingReuslt {

    private final String webRejectMessage;
    private final String batchRejectMessage;
}
