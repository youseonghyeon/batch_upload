package com.example.excel.controller;

import com.example.excel.dto.ExcelData;
import com.example.excel.dto.ExcelUploadResponse;
import com.example.excel.dto.SaveDto;
import com.example.excel.dto.SaveRequest;
import com.example.excel.repository.BatchUploadStore;
import com.example.excel.service.CsvReader;
import com.example.excel.service.ExcelReader;
import com.example.excel.service.ImageUploader;
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

import java.io.IOException;
import java.util.ArrayList;
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
    private final ImageUploader imageUploader;


    @PostMapping("/upload")
    public ResponseEntity<List<ExcelUploadResponse>> uploadExcelFile(
            @RequestParam("excelFile") MultipartFile excelFile,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageZip) throws IOException {
        List<ExcelUploadResponse> list = uploadExcelFile(excelFile);
        if (imageZip != null) {
            ImageUploadResult imageUploadResult = imageUploader.uploadImageFileFromZip(imageZip);
        }
        return ResponseEntity.ok(list);
    }


    private List<ExcelUploadResponse> uploadExcelFile(MultipartFile excelFile) {
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
        List<ExcelUploadResponse> list = saveBatchExcelDataToMemory(lists);
        // 3. validation
        Map<String, List<ValidatorBindingResult>> bindingResultMap = validateParameters(list);
        // 4. 결과 반환

//        for (String key : bindingResultMap.keySet()) {
//            List<ValidatorBindingResult> validatorBindingResults = bindingResultMap.get(key);
//            list.add(validatorBindingResults.get(0).toExcelUploadResponse(key));
//        }
        return list;
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

    private List<ExcelUploadResponse> saveBatchExcelDataToMemory(List<ExcelData> lists) {
        List<ExcelUploadResponse> responses = new ArrayList<>();

        for (ExcelData excelData : lists) {
            // 메모리 저장 후 키 발급 (메시지 ID를 사용해도 될 것 같음)
            String key = batchUploadStore.saveAndGetKey(excelData);
            ExcelUploadResponse response = new ExcelUploadResponse(key, 20240101, excelData);
            responses.add(response);
        }
        return responses;
    }

    private Map<String, List<ValidatorBindingResult>> validateParameters(List<ExcelUploadResponse> list) {
        Map<String, List<ValidatorBindingResult>> result = new LinkedHashMap<>();

        for (ExcelUploadResponse response : list) {

            ExcelData excelData = response.getExcelData();
            MessageValidator messageValidator = validatorFactory.getMessageValidator(excelData.getMessageType());

            // 테스트용 임시 데이터
            RcsMmsValidatorParam rcsMmsValidatorParam = excelData.toRcsMmsValidatorParam();
            List<ValidatorBindingResult> validate = messageValidator.validate(rcsMmsValidatorParam);

            if (!validate.isEmpty()) {
                result.put(response.getUuid(), validate);
            }
        }
        return result;
    }

}
