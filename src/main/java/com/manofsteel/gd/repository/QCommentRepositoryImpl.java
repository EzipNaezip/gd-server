package com.manofsteel.gd.repository;

import com.manofsteel.gd.type.entity.Comment;
import com.manofsteel.gd.type.entity.Post;
import com.manofsteel.gd.type.entity.QComment;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
@RequiredArgsConstructor
public class QCommentRepositoryImpl implements QCommentRepository{
    private final JPAQueryFactory queryFactory;
    private QComment qComment = QComment.comment;

    @Override
    public List<Comment> findAll(Long start, Long display, Post postNum) {
        List<Comment> results = queryFactory.selectFrom(qComment)
                .where(qComment.postNum.eq(postNum))
                .orderBy(qComment.serialNum.desc())  // Sort by createdAt field in descending order (newest first)
                .offset(start)
                .limit(display)
                .fetch();
        return results;
    }

}
