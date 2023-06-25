package com.manofsteel.gd.service;

import com.manofsteel.gd.exception.NotFoundException;
import com.manofsteel.gd.repository.CommentRepository;
import com.manofsteel.gd.repository.PostRepository;
import com.manofsteel.gd.repository.UserRepository;
import com.manofsteel.gd.type.dto.CommentDto;
import com.manofsteel.gd.type.dto.CommentRequestDto;
import com.manofsteel.gd.type.entity.Comment;
import com.manofsteel.gd.type.entity.Post;
import com.manofsteel.gd.type.entity.User;
import com.manofsteel.gd.util.EntityUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    private static final String LOGIN_REQUIRED_MSG = "로그인 후 이용해주세요";
    private static final String COMMENT_NOT_FOUND_MSG = "해당 댓글이 존재하지 않습니다.";
    private static final String WRONG_WRITER_MSG = "해당 댓글의 작성자가 아닙니다.";

    private void validateCommentWriter(Comment comment, User writer) {
        if (!comment.getWriterId().equals(writer)) {
            throw new IllegalArgumentException(WRONG_WRITER_MSG);
        }
    }
    // Constructor injection
        @Transactional
    public Comment save(CommentRequestDto dto, Long userId){
        //user가 null이라면 로그인 후 이용해주세요 라고 exception을 던져줘야함
            User user = EntityUtil.findUser(userRepository, userId);
            Post post = EntityUtil.findPost(postRepository, dto.getPostNum());

            Comment comment = Comment.builder()
                    .content(dto.getContent())
                    .postNum(post)
                    .writerId(user)
                    .build();



        return commentRepository.save(comment);
    }

    @Transactional
    public void delete(Long serialNum, Long userId){
        //comment가 존재하지 않는다면 exception을 던져줘야함
        Comment comment = EntityUtil.findComment(commentRepository, serialNum);
        User user = EntityUtil.findUser(userRepository, userId);
        validateCommentWriter(comment, user);//해당 댓글의 작성자가 맞는지 확인
        commentRepository.delete(comment);
    }
    @Transactional
    public Comment updateComment(Long serialNum,Long userId,String sentence){
        User writer = EntityUtil.findUser(userRepository, userId);
        Comment comment = EntityUtil.findComment(commentRepository, serialNum);
        validateCommentWriter(comment, writer);//해당 댓글의 작성자가 맞는지 확인
        comment.update(sentence);
        return comment;
    }
    @Transactional
    public List<Comment> findAll(Long start,Long display,Long postNum)
    {
        Post post = EntityUtil.findPost(postRepository, postNum);
        List<Comment> commentList = commentRepository.findAll(start,display,post);
        List<Comment> resultList = commentList.stream()
                .skip(start)
                .limit(display)
                .collect(Collectors.toList());
        return resultList;
    }

    @Transactional
    public boolean isMeByUser(Comment comment, User user) {
        return commentRepository.existsCommentByWriterIdAndSerialNum(user, comment.getSerialNum());
    }
}
