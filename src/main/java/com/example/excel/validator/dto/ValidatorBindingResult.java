package com.example.excel.validator.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ValidatorBindingResult {

    private final String webRejectMessage;
    private final String batchRejectMessage;
}
