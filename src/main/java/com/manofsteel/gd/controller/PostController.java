package com.manofsteel.gd.controller;

import com.manofsteel.gd.auth.util.AuthUtil;
import com.manofsteel.gd.exception.PermissionDeniedException;
import com.manofsteel.gd.exception.ThumbnailCreationException;
import com.manofsteel.gd.repository.ChatLogRepository;
import com.manofsteel.gd.repository.PostRepository;
import com.manofsteel.gd.repository.UserRepository;
import com.manofsteel.gd.service.ChatLogService;
import com.manofsteel.gd.service.PostService;
import com.manofsteel.gd.type.dto.PostDto;
import com.manofsteel.gd.type.dto.ResponseModel;
import com.manofsteel.gd.type.entity.ChatLog;
import com.manofsteel.gd.type.entity.Post;
import com.manofsteel.gd.type.entity.User;
import com.manofsteel.gd.type.vo.PostVo;
import com.manofsteel.gd.util.EntityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
@Tag(name = "Post")
@Slf4j
public class PostController{

    private final PostService postService;
    private final UserRepository userRepository;
    private final ChatLogService chatLogService;
    private final ChatLogRepository chatLogRepository;


    private final PostRepository postRepository;

    @Operation(summary="게시글 삭제", description = "게시글 작성자가 게시글 삭제" + " PathVariable : postNum(삭제할 게시글의 postNum)")
//    @Secured({"ROLE_USER","ROLE_ADMIN"})
    @DeleteMapping("/delete/{postNum}")
    public ResponseModel deletePost(
            @PathVariable("postNum") Long postNum
    ) throws PermissionDeniedException {
       Long writerId = AuthUtil.getAuthenticationInfoUserId();
   // Long writerId=EntityUtil.findUser(userRepository,1L).getUserId();
        if(writerId == null) {
            throw new IllegalArgumentException("writerId is null");
        }

        postService.delete(postNum,writerId);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

    @Operation(summary = "게시글 리스트 조회", description = "게시글 조회(default: 20개)"+
            "<br> PathVariable : start(게시글 시작 인덱스), display(게시글 마지막 인덱스)")
    //@Secured({"ROLE_USER", "ROLE_ADMIN"})
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResponseModel PostList(
            @RequestParam(name = "start", required = false, defaultValue = "0") long start,
            @RequestParam(name = "display", required = false, defaultValue = "20") long display
    ) throws ThumbnailCreationException {
        List<Post> postList = postService.findAll(start, display);
        List<PostVo> postVoList = new ArrayList<>();
        boolean isBookmarked;
        boolean isMe;
        User user = AuthUtil.getAuthenticationInfo();
        if(user ==null)
        {
            for (Post post : postList) {

                PostVo postVo = new PostVo(post.getPostNum(), post.getThumbnailImgUrl(), false,false);
                System.out.println("로그인 완");
                postVoList.add(postVo);
            }
        }
        else {
            for (Post post : postList) {

                isBookmarked = postService.isPostBookmarkedByUser(post, user);
                isMe = postService.isMeByUser(post, user);
                PostVo postVo = new PostVo(post.getPostNum(), post.getThumbnailImgUrl(), isBookmarked, isMe);
                System.out.println("로그인 완");

                postVoList.add(postVo);
            }
        }
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData("postList", postVoList);

        return responseModel;
    }

    @Operation(summary = "게시글 상세 조회", description = "게시글 상세 조회"+
            "<br> PathVariable : postNum(조회할 게시글의 postNum)")
//    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @RequestMapping(value = "/list/{postNum}", method = RequestMethod.GET)
    public ResponseModel PostDetail(
            @PathVariable("postNum") Long postNum
    ) {
        //User user = AuthUtil.getAuthenticationInfoUserId();
        User user = AuthUtil.getAuthenticationInfo();
        PostDto postDto = postService.getPost(postNum, user.getUserId());
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData("post",postDto);
        return responseModel;
    }

    @Operation(summary = "Top30 정렬", description = "Top30 정렬" + " PathVariable : start(게시글 시작 인덱스), display(게시글 마지막 인덱스)")
    //@Secured({"ROLE_USER", "ROLE_ADMIN"})
    @GetMapping("/popular")
    public ResponseModel getPopularPost(
            @RequestParam(name = "start", required = false, defaultValue = "0") long start,
            @RequestParam(name = "display", required = false, defaultValue = "20") long display
    ) {List<PostVo> postVoList = new ArrayList<>();
        List<Post> postList = postService.getPopularPosts(start, display);
        boolean isBookmarked;
        boolean isMe;
        User user = AuthUtil.getAuthenticationInfo();
        if(user ==null)
        {
            for (Post post : postList) {

                PostVo postVo = new PostVo(post.getPostNum(), post.getThumbnailImgUrl(), false,false);
                System.out.println("로그인 완");
                postVoList.add(postVo);
            }
        }
        else {
            for (Post post : postList) {

                isBookmarked = postService.isPostBookmarkedByUser(post, user);
                isMe = postService.isMeByUser(post, user);
                PostVo postVo = new PostVo(post.getPostNum(), post.getThumbnailImgUrl(), isBookmarked, isMe);
                System.out.println("로그인 완");

                postVoList.add(postVo);
            }
        }

        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData("postList", postVoList);
        return responseModel;
    }

    @Operation(summary = "태그 필터", description = "태그 필터 : " +
            "<br> PathVariable : tagName(태그 이름), start(게시글 시작 인덱스), display(게시글 마지막 인덱스)")
    //@Secured({"ROLE_USER", "ROLE_ADMIN"})
    @GetMapping("/filter/{tagName}")
    public ResponseModel getFilteredPost(
            @PathVariable String tagName,
            @RequestParam(name = "start", required = false, defaultValue = "0") long start,
            @RequestParam(name = "display", required = false, defaultValue = "20") long display
    ) throws ThumbnailCreationException {
        List<PostVo> postVoList = new ArrayList<>();
        List<Post> postList = postService.getFilteredPosts(tagName, start, display);
        ResponseModel responseModel = ResponseModel.builder().build();
        boolean isBookmarked;
        boolean isMe;
        User user = AuthUtil.getAuthenticationInfo();
        if(user ==null)
        {
            for (Post post : postList) {

                PostVo postVo = new PostVo(post.getPostNum(), post.getThumbnailImgUrl(), false,false);
                System.out.println("로그인 완");
                postVoList.add(postVo);
            }
        }
        else {
            for (Post post : postList) {

                isBookmarked = postService.isPostBookmarkedByUser(post, user);
                isMe = postService.isMeByUser(post, user);
                PostVo postVo = new PostVo(post.getPostNum(), post.getThumbnailImgUrl(), isBookmarked, isMe);
                System.out.println("로그인 완");

                postVoList.add(postVo);
            }
        }
        responseModel.addData("postList", postVoList);
        return responseModel;
    }

    @Operation(summary ="게시물 검색" , description = "검색 키워드에서 게시물 내용 검색" +
            "<br> @RequestParam : start(게시글 시작 인덱스), display(게시글 마지막 인덱스), keyword(검색 키워드)")
    //@Secured({"ROLE_USER", "ROLE_ADMIN"})
    @GetMapping("/search")
    public ResponseModel searchPost(
            @RequestParam(name = "start", required = false, defaultValue = "0") long start,
            @RequestParam(name = "display", required = false, defaultValue = "20") long display,
            @RequestParam(name = "keyword", required = false, defaultValue = "") String keyword
    ) throws ThumbnailCreationException { List<PostVo> postVoList = new ArrayList<>();
        List<Post> postList = postService.searchPost(keyword,start,display);
        boolean isBookmarked;
        boolean isMe;
        User user = AuthUtil.getAuthenticationInfo();
        if(user ==null)
        {
            for (Post post : postList) {

                PostVo postVo = new PostVo(post.getPostNum(), post.getThumbnailImgUrl(), false,false);
                System.out.println("로그인 완");
                postVoList.add(postVo);
            }
        }
        else {
            for (Post post : postList) {

                isBookmarked = postService.isPostBookmarkedByUser(post, user);
                isMe = postService.isMeByUser(post, user);
                PostVo postVo = new PostVo(post.getPostNum(), post.getThumbnailImgUrl(), isBookmarked, isMe);
                System.out.println("로그인 완");

                postVoList.add(postVo);
            }
        }
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData("postList", postVoList);
        return responseModel;
    }
}
