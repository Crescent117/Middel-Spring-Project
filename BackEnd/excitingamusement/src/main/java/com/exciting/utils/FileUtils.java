package com.exciting.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;

public class FileUtils {
    public static void createDirectory(String fileUploadDirectory) {
        if (StringUtils.isEmpty(fileUploadDirectory)) {
            throw new IllegalArgumentException("fileUploadDirectory must not be null or empty");
        }

        try {
            Resource resource = new PathMatchingResourcePatternResolver().getResource(fileUploadDirectory);
            File directory = resource.getFile();

            if (!directory.exists()) {
                directory.mkdirs();
            }
        } catch (IOException e) {
            // 예외 처리 코드 작성
            e.printStackTrace();
        }
    }
}