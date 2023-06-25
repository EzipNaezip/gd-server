package com.manofsteel.gd.repository;

import com.manofsteel.gd.type.entity.Comment;
import com.manofsteel.gd.type.entity.Post;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface QCommentRepository {

    List<Comment> findAll(Long start, Long display, Post postNum);
}
