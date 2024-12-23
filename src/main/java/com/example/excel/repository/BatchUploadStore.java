package com.example.excel.repository;

import com.example.excel.controller.ExcelData;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class BatchUploadStore {

    private static final long TTL_MILLIS = 10 * 60 * 1000; // TTL: 10분
    private final Map<String, StoredData> excelDataStore = new ConcurrentHashMap<>();

    public String saveAndGetKey(ExcelData excelData) {
        String key = UUID.randomUUID().toString();
        StoredData storedData = new StoredData(excelData, Instant.now().toEpochMilli() + TTL_MILLIS);

        log.info("[Batch Upload save()] key: {} , storedData: {}", key, storedData.toString().replace("\n", "\\n"));
        excelDataStore.put(key, storedData);
        return key;
    }

    public ExcelData getExcelData(String key) {
        StoredData storedData = excelDataStore.get(key);
        if (storedData == null || isExpired(storedData)) {
            excelDataStore.remove(key); // 만료된 데이터는 삭제
            return null;
        }
        return storedData.getExcelData();
    }

    @Scheduled(fixedRate = 60_000) // 1분마다 실행
    public void cleanupExpiredData() {
        long currentTime = Instant.now().toEpochMilli();
        excelDataStore.entrySet().removeIf(entry -> entry.getValue().getExpiryTime() < currentTime);
    }

    private boolean isExpired(StoredData storedData) {
        return storedData.getExpiryTime() < Instant.now().toEpochMilli();
    }

    @Data
    private static class StoredData {
        private final ExcelData excelData;
        private final long expiryTime;

        public StoredData(ExcelData excelData, long expiryTime) {
            this.excelData = excelData;
            this.expiryTime = expiryTime;
        }

    }
}
