package com.example.electricbillsystem.upload_download;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "file")
@Data
public class FileStorageProperties {

    private String uploadDir;

}
