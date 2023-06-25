package com.manofsteel.gd.repository;

import java.util.List;
import java.util.Optional;

import com.manofsteel.gd.type.entity.QUser;
import com.manofsteel.gd.type.entity.User;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class QUserRepositoryImpl implements QUserRepository {

    private final JPAQueryFactory queryFactory;
    private QUser qUser = QUser.user;

    @Override
    public List<User> findAll(Long start, Long display) {
        List<User> results = queryFactory.selectFrom(qUser)
                .offset(start)
                .limit(display)
                .fetch();
        return results;
    }

    @Override
    public Optional<User> findByUserId(Long userId) {
        User user = queryFactory.selectFrom(qUser)
                .where(qUser.userId.eq(userId))
                .fetchOne();
        return Optional.ofNullable(user);
    }
}