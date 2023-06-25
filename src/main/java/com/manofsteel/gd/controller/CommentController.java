package com.manofsteel.gd.controller;

import com.manofsteel.gd.auth.util.AuthUtil;
import com.manofsteel.gd.repository.UserRepository;
import com.manofsteel.gd.service.CommentService;
import com.manofsteel.gd.type.dto.CommentDto;
import com.manofsteel.gd.type.dto.CommentRequestDto;
import com.manofsteel.gd.type.dto.InputDto;
import com.manofsteel.gd.type.dto.ResponseModel;
import com.manofsteel.gd.type.entity.Comment;
import com.manofsteel.gd.type.entity.User;
import com.manofsteel.gd.util.EntityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
@Tag(name = "Comment")
@Slf4j
public class CommentController {

    private final CommentService commentService;
private final UserRepository userRepository;
    @Operation(summary="댓글 작성",description = "로그인을 한 유저가 댓글을 작성" +
            "<br> 매개변수 : CommentRequestDto : content(댓글 내용), writerId(User의 userId), postNum(게시글 번호)"+
            "<br> { \"content\" : \"댓글 내용\", \"postNum\" : 1 }")
//    @Secured({"ROLE_USER","ROLE_ADMIN"})
    @PostMapping("")
    public ResponseModel createComment(@RequestBody CommentRequestDto commentDto) {
        User writerId = AuthUtil.getAuthenticationInfo();
        //User writerId = EntityUtil.findUser(userRepository,1L);
        Comment comment = commentService.save(commentDto,writerId.getUserId());
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

    @Operation(summary="댓글 수정",description = "댓글 작성자가 댓글을 수정"
            +"<br>매개변수 : @RequestBody InputDto: String sentence" +
            "<br> PathVariable : serialNum(댓글 번호)")
//    @Secured({"ROLE_USER","ROLE_ADMIN"})
    @PutMapping("/{serialNum}/update")
    public ResponseModel updateComment(@RequestBody InputDto inputDto, @PathVariable Long serialNum){
        String sentence= inputDto.getSentence();
        User writerId = AuthUtil.getAuthenticationInfo();
        //User writerId = EntityUtil.findUser(userRepository,1L);
        Comment comment= commentService.updateComment(serialNum,writerId.getUserId(),sentence);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData("updatedComment", comment);
        return responseModel;
    }

    @Operation(summary="댓글 삭제",description = "댓글 작성자가 댓글 삭제" + " PathVariable : serialNum(댓글 번호)")
//    @Secured({"ROLE_USER","ROLE_ADMIN"})
    @DeleteMapping("/{serialNum}/delete")
    public ResponseModel deleteComment(@PathVariable("serialNum") Long serialNum) {
        User writerId = AuthUtil.getAuthenticationInfo();

        //User writerId = EntityUtil.findUser(userRepository,1L);
        commentService.delete(serialNum,writerId.getUserId());
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

    @Operation(summary="댓글 리스팅",description = "댓글 리스트를 리스팅" + " @PathVariable : postNum(게시글 번호)" +
            "<br> @RequestParam : start(시작 인덱스), display(마지막 인덱스)")
//    @Secured({"ROLE_USER","ROLE_ADMIN"})
    @GetMapping("/list/{postNum}")
    public ResponseModel getCommentList( @RequestParam(name = "start", required = false, defaultValue = "0") long start,
                                         @RequestParam(name = "display", required = false, defaultValue = "20") long display,
                                         @PathVariable Long postNum) {
        //User writerId = EntityUtil.findUser(userRepository,1L);
        User writerId = AuthUtil.getAuthenticationInfo();
        List<Comment> commentList = commentService.findAll(start,display,postNum);
        List<CommentDto> list=new ArrayList<>();
        CommentDto commentDto;
        Boolean isMe=false;
        if(writerId!=null)
        {
        for(Comment comments: commentList)
        {
            list.add(commentDto = CommentDto.builder()
                            .content(comments.getContent())
                            .serialNum(comments.getSerialNum())
                            .user(comments.getWriterId())

                            .isMe(commentService.isMeByUser(comments,writerId))
                    .build());
        }
        }
        else {
            for(Comment comments: commentList)
            {
                list.add(commentDto = CommentDto.builder()
                        .content(comments.getContent())
                        .serialNum(comments.getSerialNum())
                        .user(comments.getWriterId())
                        .isMe(false)
                        .build());
            }
        }
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData("commentList", list);
        return responseModel;
    }


}