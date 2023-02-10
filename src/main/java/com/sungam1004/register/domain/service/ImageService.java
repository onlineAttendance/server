package com.sungam1004.register.domain.service;

import com.sungam1004.register.global.exception.CustomException;
import com.sungam1004.register.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ImageService {

    @Value("${image.path}")
    private String imagePath;

    @Value("${image.default_filename}")
    private String defaultImageName;

    public String registryImage(MultipartFile faceImageFile) {
        try {
            if (faceImageFile == null || faceImageFile.isEmpty()) return defaultImageName;
            String imageFullName = createStoreFileName(faceImageFile.getOriginalFilename());
            faceImageFile.transferTo(new File(imagePath + "/" + imageFullName));
            return imageFullName;
        } catch (IOException e) {
            e.printStackTrace();
            throw new CustomException(ErrorCode.FAIL_STORE_IMAGE);
        }
    }

    private String createStoreFileName(String originalFileName) {
        String ext = extractExt(originalFileName);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }

    private String extractExt(String originalFileName) {
        int pos = originalFileName.lastIndexOf(".");
        return originalFileName.substring(pos + 1);
    }

    public UrlResource getImageFile(String fileName) {
        try {
            return new UrlResource("file:" + imagePath + "/" + fileName);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(ErrorCode.FAIL_CALL_IMAGE);
        }
    }
}
