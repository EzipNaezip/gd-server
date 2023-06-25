package com.manofsteel.gd.controller;

import com.manofsteel.gd.auth.util.AuthUtil;
import com.manofsteel.gd.repository.UserRepository;
import com.manofsteel.gd.service.TagService;
import com.manofsteel.gd.type.dto.PostDto;
import com.manofsteel.gd.type.dto.ResponseModel;
import com.manofsteel.gd.type.entity.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tags")
@RequiredArgsConstructor
@io.swagger.v3.oas.annotations.tags.Tag(name = "Tag")
@Slf4j
public class TagController {
private final UserRepository userRepository;
    private final TagService tagService;

    @Operation(summary="태그로 포스트 목록 가져오기",description = "태그 검색으로 포스트 목록 가져오기" + " @RequestBody : tagName(태그 이름)"+
    "<br> { \"tagName\" : \"태그 이름\" }")
//    @Secured({"ROLE_USER","ROLE_ADMIN"})
    @PostMapping("/list")
    public ResponseModel getPostsWithInfoByTag(@RequestBody String tagName) {
       Long userId = AuthUtil.getAuthenticationInfoUserId();

        //Long userId= EntityUtil.findUser(userRepository,1L).getUserId();
        List<PostDto> postList = tagService.getPostsWithInfoByTag(tagName, userId);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData("post",postList);
        return responseModel;
    }

    @Operation(summary = "게시물에서 태그 추출",description = "저장된 태그를 통한 게시물에서 태그 추출" + " PathVariable : postNum(게시물 번호)")
    //@Secured({"ROLE_USER","ROLE_ADMIN"})
    @GetMapping("/extractPost/{postNum}")
    public ResponseModel extractTagFromPost(@PathVariable Long postNum){
        List<Tag> tagList = tagService.getTagsByPost(postNum);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData("tag",tagList);
        return responseModel;
    }

}