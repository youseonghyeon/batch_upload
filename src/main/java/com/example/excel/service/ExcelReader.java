package com.example.excel.service;

import com.example.excel.dto.ExcelData;
import com.example.excel.controller.ReflectionMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class ExcelReader {

    private static final int SHEET_INDEX = 0; // 기본 읽을 시트 인덱스
    private static final int START_ROW = 1; // 데이터 시작 행 (헤더 제외)
    private static final int START_COLUMN = 0; // 데이터 시작 열
    private static final int CELL_SIZE = 54; // 열 크기

    private final DataFormatter dataFormatter = new DataFormatter(); // 셀 포맷터

    public boolean supports(String originalFilename) {
        return originalFilename.endsWith(".xlsx") || originalFilename.endsWith(".xls");
    }

    public List<ExcelData> read(MultipartFile file) {
        // 파일 확장자 확인
        if (!supports(Objects.requireNonNull(file.getOriginalFilename()))) {
            throw new IllegalArgumentException("Invalid file type. Only Excel files are allowed.");
        }
        try (InputStream inputStream = file.getInputStream()) {
            return readExcel(inputStream);
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
    private List<ExcelData> readExcel(InputStream inputStream) {
        try (Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(SHEET_INDEX); // 읽을 시트 선택
            List<List<String>> rows = extractRows(sheet); // 데이터 추출
            return mapRowsToEntities(rows, ExcelData.class); // 엔티티로 변환
        } catch (Exception e) {
            throw new RuntimeException("Error reading Excel file", e);
        }
    }

    /**
     * Sheet에서 데이터를 추출하여 List<List<String>> 형태로 반환
     *
     * @param sheet 대상 Sheet
     * @return 행 데이터 리스트
     */
    private List<List<String>> extractRows(Sheet sheet) {
        List<List<String>> rows = new ArrayList<>();

        for (Row row : sheet) {
            if (row.getRowNum() < START_ROW) {
                continue; // 시작 행 이전은 무시
            }
            List<String> rowData = extractRowData(row); // 행 데이터 추출
            rows.add(rowData);
        }

        return rows;
    }

    /**
     * 단일 Row에서 데이터를 추출하여 List<String> 형태로 반환
     *
     * @param row 대상 Row
     * @return 셀 데이터 리스트
     */
    private List<String> extractRowData(Row row) {
        List<String> rowData = new ArrayList<>();

        for (int colIndex = START_COLUMN; colIndex < START_COLUMN + CELL_SIZE; colIndex++) {
            Cell cell = row.getCell(colIndex);
            rowData.add(getCellValue(cell));
        }

        return rowData;
    }

    private <T> List<T> mapRowsToEntities(List<List<String>> rows, Class<T> clazz) {
        return rows.stream()
                .map(row -> ReflectionMapper.mapValuesToInstance(clazz, row))
                .toList();
    }

    // cell 이 null이면 ""을 반환함
    private String getCellValue(Cell cell) {
        return dataFormatter.formatCellValue(cell).trim();
    }

}
