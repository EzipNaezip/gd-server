package com.manofsteel.gd.controller;

import com.manofsteel.gd.auth.util.AuthUtil;
import com.manofsteel.gd.service.LikesService;
import com.manofsteel.gd.type.dto.ResponseModel;
import com.manofsteel.gd.type.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/likes")
@RequiredArgsConstructor
@Tag(name = "Likes")
@Slf4j
public class LikeController {
    private final LikesService likesService;


    @Operation(summary = "좋아요", description = "@PathVariable postNum(게시물번호)")
    @PostMapping("/create/{postNum}")
    public ResponseModel createlikes(@PathVariable("postNum") Long postNum) {

       User userId = AuthUtil.getAuthenticationInfo();
        if(userId == null) {
            throw new IllegalArgumentException("로그인 후 이용해주세요");
        }
        likesService.likePost(postNum,userId);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }
    @Operation(summary = "좋아요 해제", description = "@PathVariable postNum(게시물번호)")
    @DeleteMapping("/delete/{postNum}")
    public ResponseModel deleteLikes(@PathVariable("postNum") Long postNum) {
        User userId = AuthUtil.getAuthenticationInfo();
        if(userId == null) {
            throw new IllegalArgumentException("로그인 후 이용해주세요");
        }
        likesService.unlike(postNum,userId);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }


}
