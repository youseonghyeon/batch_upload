package com.example.excel.service;

import com.example.excel.controller.ExcelData;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class CsvReader {

    private static final int SHEET_INDEX = 0; // 기본 읽을 시트 인덱스
    private static final int START_ROW = 1; // 데이터 시작 행 (헤더 제외)
    private static final int START_COLUMN = 0; // 데이터 시작 열
    private static final int CELL_SIZE = 54; // 열 크기

    private final DataFormatter dataFormatter = new DataFormatter(); // 셀 포맷터

    public boolean supports(String originalFilename) {
        return originalFilename.endsWith(".csv");
    }

    public List<ExcelData> read(MultipartFile file) {
        // 파일 확장자 확인
        supports(Objects.requireNonNull(file.getOriginalFilename()));
        try (InputStream inputStream = file.getInputStream()) {
            return readCsv(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Error reading Excel file", e);
        }
    }

    /**
     * Excel 파일을 읽어 List<ExcelData>로 변환
     *
     * @param inputStream 업로드된 Excel 파일 InputStream
     * @return ExcelData 리스트
     */
    private List<ExcelData> readCsv(InputStream inputStream) {
        // TODO : CSV 파일 읽기 구현 필요
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Sheet에서 데이터를 추출하여 List<List<String>> 형태로 반환
     *
     * @param sheet 대상 Sheet
     * @return 행 데이터 리스트
     */
    private List<List<String>> extractRows(Sheet sheet) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * 단일 Row에서 데이터를 추출하여 List<String> 형태로 반환
     *
     * @param row 대상 Row
     * @return 셀 데이터 리스트
     */
    private List<String> extractRowData(Row row) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    private <T> List<T> mapRowsToEntities(List<List<String>> rows, Class<T> clazz) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    // cell 이 null이면 ""을 반환함
    private String getCellValue(Cell cell) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

}
