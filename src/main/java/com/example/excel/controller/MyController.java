package com.example.excel.controller;

import com.example.excel.dto.SaveDto;
import com.example.excel.dto.SaveRequest;
import com.example.excel.repository.BatchUploadStore;
import com.example.excel.service.CsvReader;
import com.example.excel.service.ExcelReader;
import com.example.excel.validator.MessageValidator;
import com.example.excel.validator.ValidatorFactory;
import com.example.excel.validator.dto.RcsMmsValidatorParam;
import com.example.excel.validator.dto.ValidatorBindingResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MyController {

    private final ValidatorFactory validatorFactory;
    private final ExcelReader excelReader;
    private final CsvReader csvReader;
    private final BatchUploadStore batchUploadStore;

    @PostMapping("/upload")
    public ResponseEntity<Map> uploadExcelFile(@RequestParam("excelFile") MultipartFile excelFile, @RequestParam("imageFile") MultipartFile imageFile) {
        List<ExcelData> lists = null;
        // TODO 스프레드 시트 reader 인터페이스 사용해야 하며, default 메서드로 Factory Method 를 구현해야 함(적합한 Reader를 찾는거를)
        // 1. 엑셀 read
        if (excelReader.supports(excelFile.getOriginalFilename())) {
            lists = excelReader.read(excelFile);
        } else if (csvReader.supports(excelFile.getOriginalFilename())) {
            lists = csvReader.read(excelFile);
        } else {
            throw new IllegalArgumentException("Not supported file type");
        }
        // TODO ----------------------------------------

        // 2. 메모리 저장
        Map<String, Object> map = saveBatchExcelDataToMemory(lists);
        // 3. validation
        Map<String, List<ValidatorBindingResult>> bindingResultMap = validateParameters(map);
        // 4. 결과 반환
        for (String key : bindingResultMap.keySet()) {
            map.put(key, bindingResultMap.get(key));
        }
        return ResponseEntity.ok(map);

    }

    @PostMapping("/test-send")
    public ResponseEntity<String> testSend(@RequestBody SaveRequest keys) {
        // 값 호출
        List<ExcelData> findExcelDataList = batchUploadStore.findAllByKeys(keys.getKey());
        // 값 변환
        List<TestSendDto> list = findExcelDataList.stream().map(ExcelData::toTestSendDto).toList();
        // 시험발송
        for (TestSendDto testSendDto : list) {
            log.info("testSend(testSendDto) 실행");
        }

        return ResponseEntity.ok("success");
    }

    @PostMapping("/save")
    public ResponseEntity<List<ExcelData>> saveExcelData(@RequestBody SaveRequest keys) {
        // 값 호출
        List<ExcelData> findExcelDataList = batchUploadStore.findAllByKeys(keys.getKey());
        // 값 변환
        List<SaveDto> list = findExcelDataList.stream().map(ExcelData::toSaveDto).toList();
        // 템플릿 저장
        for (SaveDto saveDto : list) {
            log.info("saveMessage(saveDto) 실행");
        }

        return ResponseEntity.ok(findExcelDataList);
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

}
