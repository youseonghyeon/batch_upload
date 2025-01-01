package com.example.excel.config;

import com.example.excel.service.ImageUploader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    private static final String IMAGE_BACKUP_DIR = "/appdata/backup/";
    private static final String IMAGE_DIR = "/appdata/images/";

    @Bean
    public ImageUploader imageUploader() {
        String IDEA_PATH = System.getProperty("user.dir");
        return new ImageUploader(IDEA_PATH + IMAGE_BACKUP_DIR, IDEA_PATH + IMAGE_DIR);
    }

}
