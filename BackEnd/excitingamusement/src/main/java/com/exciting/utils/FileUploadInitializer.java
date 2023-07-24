package com.exciting.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class FileUploadInitializer implements ApplicationRunner {
    private final String fileUploadDirectory;

    public FileUploadInitializer(Environment environment) {
        this.fileUploadDirectory = environment.getProperty("file.upload.directory");
    }

    @Override
    public void run(ApplicationArguments args) {
        FileUtils.createDirectory(fileUploadDirectory);
    }
}