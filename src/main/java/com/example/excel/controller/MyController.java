package com.example.excel.controller;

import com.example.excel.repository.BatchUploadStore;
import com.example.excel.service.ExcelReader;
import com.example.excel.validator.MessageValidator;
import com.example.excel.validator.ValidatorFactory;
import com.example.excel.validator.dto.RcsMmsValidatorParam;
import com.example.excel.validator.dto.ValidatorBindingResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MyController {

    private final ValidatorFactory validatorFactory;
    private final ExcelReader excelReader;
    private final ObjectMapper objectMapper;
    private final BatchUploadStore batchUploadStore;

    @PostMapping("/upload")
    public ResponseEntity<Map> uploadExcelFile(@RequestParam("file") MultipartFile file) throws Exception {
        // 1. 엑셀 read
        List<ExcelData> lists = readExcelDatas(file);
        // 2. db 저장
        Map<String, Object> map = saveBatchExcelDataToMemory(lists);
        // 3. validation
        Map<String, List<ValidatorBindingResult>> bindingResultMap = validateParameters(map);

        // 4. 결과 반환
        for (String key : bindingResultMap.keySet()) {
            map.put(key, bindingResultMap.get(key));
        }
        return ResponseEntity.ok(map);
    }

    private List<ExcelData> readExcelDatas(MultipartFile file) throws IOException {
        // 파일 확장자 확인
        if (!file.getOriginalFilename().endsWith(".xlsx") && !file.getOriginalFilename().endsWith(".xls")) {
            throw new IllegalArgumentException("Invalid file type. Only Excel files are allowed.");
        }
        InputStream inputStream = file.getInputStream();
        return excelReader.readExcel(inputStream);
    }

    private Map<String, Object> saveBatchExcelDataToMemory(List<ExcelData> lists) {
        Map<String, Object> map = new LinkedHashMap<>();

        for (ExcelData excelData : lists) {
            // 메모리 저장 후 키 발급 (메시지 ID를 사용해도 될 것 같음)
            String key = batchUploadStore.saveAndGetKey(excelData);
            map.put(key, excelData);
        }
        return map;
    }

    private Map<String, List<ValidatorBindingResult>> validateParameters(Map<String, Object> map) {
        Map<String, List<ValidatorBindingResult>> result = new LinkedHashMap<>();
        for (String key : map.keySet()) {
            ExcelData excelData = batchUploadStore.getExcelData(key);
            MessageValidator messageValidator = validatorFactory.getMessageValidator(excelData.getMessageType());

            // 테스트용 임시 데이터
            RcsMmsValidatorParam rcsMmsValidatorParam = excelData.toRcsMmsValidatorParam();
            List<ValidatorBindingResult> validate = messageValidator.validate(rcsMmsValidatorParam);

            if (!validate.isEmpty()) {
                result.put(key, validate);
            }
        }
        return result;
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveExcelData(@RequestParam("key") String key) {
        ExcelData excelData = batchUploadStore.getExcelData(key);
        if (excelData == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Data not found or expired");
        }
        ExcelData excelData1 = batchUploadStore.getExcelData(key);
        log.info("{}", excelData1);

        return ResponseEntity.ok(excelData1 != null ? excelData1.toString() : "Data not found or expired");
    }

}
