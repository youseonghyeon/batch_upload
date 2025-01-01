package com.example.excel.dto;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ExcelUploadResponse {

    private String uuid;
    private Integer messageId;
    private ExcelData excelData;
    private Boolean hasError = false;
    private List<String> errorMessages = new ArrayList<>();

    public void rejectMessage(String errorMessage) {
        this.hasError = true;
        this.errorMessages.add(errorMessage);
    }

    public ExcelUploadResponse(String uuid, Integer messageId, ExcelData excelData) {
        this.uuid = uuid;
        this.messageId = messageId;
        this.excelData = excelData;
    }
}
