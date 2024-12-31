package com.example.excel.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.function.Function;

public interface SpreadsheetReader<T, I, O> {

    boolean supports(String originalFilename);

    List<T> read(MultipartFile file, Function<I, O> converter);
}
