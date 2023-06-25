package com.manofsteel.gd.controller;

import com.manofsteel.gd.auth.util.AuthUtil;
import com.manofsteel.gd.repository.BookmarkRepository;
import com.manofsteel.gd.repository.PostRepository;
import com.manofsteel.gd.repository.UserRepository;
import com.manofsteel.gd.service.BookmarkService;
import com.manofsteel.gd.type.dto.ResponseModel;
import com.manofsteel.gd.type.entity.Post;
import com.manofsteel.gd.type.entity.User;
import com.manofsteel.gd.util.EntityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bookmarks")
@RequiredArgsConstructor
@Tag(name = "BookMark")
@Slf4j
public class BookMarkController {
private final UserRepository userRepository;

    private final BookmarkService bookmarkService;
    @Operation(summary = "북마크 생성", description = "@PathVariable postNum(게시물번호)")
    @PostMapping("/create/{postNum}")
    public ResponseModel createBookMark(@PathVariable("postNum") Long postNum) {

         User userId = AuthUtil.getAuthenticationInfo();
         if(userId == null) {
             throw new IllegalArgumentException("로그인 후 이용해주세요");
         }
        bookmarkService.bookmarkPost(postNum,userId);
         ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }
    @Operation(summary = "북마크 해제", description = "@PathVariable postNum(게시물번호)")
    @DeleteMapping("/delete/{postNum}")
    public ResponseModel deleteBookMark(@PathVariable("postNum") Long postNum) {
        User userId = AuthUtil.getAuthenticationInfo();
        if(userId == null) {
            throw new IllegalArgumentException("로그인 후 이용해주세요");
        }
        bookmarkService.unbookmarkPost(postNum,userId);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }


}
