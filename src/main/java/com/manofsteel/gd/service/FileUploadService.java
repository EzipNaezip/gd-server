package com.manofsteel.gd.service;

import com.manofsteel.gd.exception.FileUploadFailException;
import com.manofsteel.gd.handler.FileHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileUploadService {

    private final FileHandler fileHandler;

    @Value("${resource.file.url}")
    private String fileURL;

    public String saveFile(Long postId, MultipartFile multipartFile) throws FileUploadFailException {
        String realFilename = fileHandler.saveFile(postId, multipartFile);
        return fileURL + realFilename;
    }
}