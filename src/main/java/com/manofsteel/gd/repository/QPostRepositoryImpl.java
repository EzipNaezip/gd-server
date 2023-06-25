package com.manofsteel.gd.repository;

import com.manofsteel.gd.type.entity.Post;
import com.manofsteel.gd.type.entity.QPost;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class QPostRepositoryImpl implements QPostRepository {

    private final JPAQueryFactory queryFactory;
    private QPost qPost = QPost.post;

    @Override
    public List<Post> findAll(Long start, Long display) {
        List<Post> results = queryFactory.selectFrom(qPost)
                .orderBy(qPost.postNum.desc())  //
                .offset(start)
                .limit(display)
                .fetch();
        return results;
    }

}