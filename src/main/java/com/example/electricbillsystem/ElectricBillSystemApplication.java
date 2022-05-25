package com.example.electricbillsystem;

import com.example.electricbillsystem.upload_download.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@ComponentScan
@EnableConfigurationProperties({
        FileStorageProperties.class
})
public class ElectricBillSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(ElectricBillSystemApplication.class, args);
    }

}
