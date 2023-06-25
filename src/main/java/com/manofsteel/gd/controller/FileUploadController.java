package com.manofsteel.gd.controller;

import com.manofsteel.gd.exception.FileUploadFailException;
import com.manofsteel.gd.service.FileUploadService;
import com.manofsteel.gd.type.dto.ResponseModel;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Tag(name = "File Upload")
@Slf4j
public class FileUploadController {

    private final FileUploadService fileUploadService;

    @Operation(summary = "파일 업로드", description = "ROLE_USER / ROLE_ADMIN" + " 매개변수 : id(게시글 번호), file(파일)")
//    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public ResponseModel upload(
            @RequestParam(value = "id", required = false) Long postId,
            @RequestParam(value = "file", required = true) MultipartFile multipartFile
    ) throws FileUploadFailException {
        String url = fileUploadService.saveFile(postId, multipartFile);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData("url", url);
        return responseModel;
    }

}