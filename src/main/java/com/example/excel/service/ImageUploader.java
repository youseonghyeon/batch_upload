package com.example.excel.service;

import com.example.excel.controller.ImageInfo;
import com.example.excel.controller.ImageUploadResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Slf4j
@RequiredArgsConstructor
public class ImageUploader {

    private final String BACKUP_DIR;
    private final String IMAGE_DIR;

    public ImageUploadResult uploadImageFileFromZip(MultipartFile file) {
        try {
            // 1. ZIP 파일 백업 디렉토리에 저장
            File backupZip = saveBackupZip(file);

            // 2. ZIP 파일 압축 해제 및 이미지 추출
            List<ImageInfo> images = extractImagesFromZip(backupZip);

            log.info("Extracted images: {}", images);
            List<String> uploadedImageNames = images.stream().map(ImageInfo::getName).toList();
            return new ImageUploadResult(images.size(), uploadedImageNames);
        } catch (Exception e) {
            log.error("Error occurred while processing image file", e);
            throw new RuntimeException("Error occurred while processing image file", e);
        }
    }

    private File saveBackupZip(MultipartFile file) throws IOException {
        createDirectoryIfNotExists(BACKUP_DIR);

        String originalFilename = file.getOriginalFilename() != null ? file.getOriginalFilename() : "unknown.zip";
        String nowDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

        File backupZip = new File(BACKUP_DIR, nowDateTime + "_" + originalFilename);
        file.transferTo(backupZip);
        return backupZip;
    }


    private List<ImageInfo> extractImagesFromZip(File zipFile) throws IOException {
        List<ImageInfo> imageInfoList = new ArrayList<>();
        File imageDir = createDirectoryIfNotExists(IMAGE_DIR);

        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile))) {
            ZipEntry zipEntry;
            while ((zipEntry = zis.getNextEntry()) != null) {
                // 디렉토리가 아니라 파일인 경우 처리
                if (isImagesFile(zipEntry)) {

                    File imageFile = new File(imageDir, new File(zipEntry.getName()).getName());
                    // 이미지 파일 저장
                    saveImageFileToImageDir(imageFile, zis);
                    // 이미지 정보 추출
                    ImageInfo imageInfo = extractImageInfo(imageFile);

                    imageInfoList.add(imageInfo);
                }
            }
        } catch (IOException e) {
            log.error("Error occurred while extracting images from zip file", e);
            throw e;
        }
        return imageInfoList;
    }

    private ImageInfo extractImageInfo(File imageFile) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(imageFile);
        if (bufferedImage != null) {
            ImageInfo imageInfo = new ImageInfo(
                    imageFile.getName(),
                    imageFile.length(),
                    bufferedImage.getWidth(),
                    bufferedImage.getHeight()
            );
            return imageInfo;
        } else {
            throw new IOException("Failed to read image file: " + imageFile.getName());
        }
    }

    private static void saveImageFileToImageDir(File imageFile, ZipInputStream zis) throws IOException {
        // 이미지 파일 저장
        try (FileOutputStream fos = new FileOutputStream(imageFile)) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = zis.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }
        }
    }

    private boolean isImagesFile(ZipEntry zipEntry) {
        return !zipEntry.isDirectory() && isImage(zipEntry.getName());
    }

    private File createDirectoryIfNotExists(String directoryFullPath) {
        File directoryFile = new File(directoryFullPath);
        if (!directoryFile.exists()) {
            directoryFile.mkdirs();
        }
        return directoryFile;
    }

    private boolean isImage(String fileName) {
        String lowerCaseName = fileName.toLowerCase();
        return lowerCaseName.endsWith(".jpg") || lowerCaseName.endsWith(".jpeg") ||
                lowerCaseName.endsWith(".png") || lowerCaseName.endsWith(".bmp") ||
                lowerCaseName.endsWith(".gif");
    }
}
