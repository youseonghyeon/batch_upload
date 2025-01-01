package com.example.excel.controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ImageInfo {

    private String name;
    private long length;
    private int width;
    private int height;
    private int size;

    public ImageInfo(String name, long length, int width, int height) {
        this.name = name;
        this.length = length;
        this.width = width;
        this.height = height;
        this.size = width * height;
    }
}
