package com.manofsteel.gd.controller;

import com.manofsteel.gd.auth.util.AuthUtil;
import com.manofsteel.gd.service.ApiKeyService;
import com.manofsteel.gd.type.dto.ApiKeyRequestDto;
import com.manofsteel.gd.type.dto.ApiKeyResponseDto;
import com.manofsteel.gd.type.dto.ResponseModel;
import com.manofsteel.gd.type.entity.ApiKey;
import com.manofsteel.gd.type.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api-keys")
@RequiredArgsConstructor
@Tag(name = "Api Key")
@Slf4j
public class ApiKeyController {

    private final ApiKeyService apiKeyService;

    @Operation(summary = "OpenAI API 키 등록", description = "ROLE_USER / ROLE_ADMIN")
//    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @RequestMapping(method = RequestMethod.POST)
    public ResponseModel createApiKey(@RequestBody @Valid ApiKeyRequestDto requestDto) {
        User user = AuthUtil.getAuthenticationInfo();
        apiKeyService.createApiKey(requestDto.getApiKey(), user);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

    @Operation(summary = "OpenAI API 키 수정", description = "ROLE_USER / ROLE_ADMIN")
//    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseModel updateApiKey(@RequestBody @Valid ApiKeyRequestDto requestDto) {
        User user = AuthUtil.getAuthenticationInfo();
        apiKeyService.updateApiKey(user, requestDto.getApiKey());
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

    @Operation(summary = "OpenAI API 키 삭제", description = "ROLE_USER / ROLE_ADMIN")
//    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseModel deleteApiKey() {
        User user = AuthUtil.getAuthenticationInfo();
        apiKeyService.deleteApiKey(user);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }


    @Operation(summary = "유저 API 키 리스트 조회", description = "ROLE_USER / ROLE_ADMIN")
//    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResponseModel getApiKey() {
        User user = AuthUtil.getAuthenticationInfo();
        ApiKey apiKey = apiKeyService.findByUser(user);
        ApiKeyResponseDto responseDto = new ApiKeyResponseDto(apiKey.getApiKey());
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData("ApiKeyResponse", responseDto);
        return responseModel;
    }

}
