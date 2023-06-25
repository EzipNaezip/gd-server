package com.manofsteel.gd.repository;

import com.manofsteel.gd.type.entity.Follow;
import com.manofsteel.gd.type.entity.User;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class FollowRepositoryImpl implements FollowRepository{
    private final EntityManager em;

    public FollowRepositoryImpl(EntityManager em) {
        this.em = em;
    }
    @Override
    public List<Follow> findByFollower(User follower) {
        return em.createQuery("select f from Follow f where f.follower = :follower", Follow.class)
                .setParameter("follower", follower)
                .getResultList();
    }


    @Override
    public List<Follow> findByFollowing(User following) {
        return em.createQuery("select f from Follow f where f.following = :following", Follow.class)
                .setParameter("following", following)
                .getResultList();

    }
    @Override
    public Follow save(Follow follow) {
        em.persist(follow);
        return follow;
    }

    @Override
    public Optional<Follow> findByFollowerAndFollowing(User follower, User following) {
        return em.createQuery("select f from Follow f where f.follower = :follower and f.following = :following", Follow.class)
                .setParameter("follower", follower)
                .setParameter("following", following)
                .getResultList().stream().findAny();
    }


    @Override
    public Boolean existsByFollowerIdAndFollowingId(Long followerId, Long followingId) {
        return em.createQuery("select count(f) from Follow f where f.follower.userId = :followerId and f.following.userId = :followingId", Long.class)
                .setParameter("followerId", followerId)
                .setParameter("followingId", followingId)
                .getSingleResult() > 0;
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends Follow> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends Follow> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<Follow> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Follow getOne(Long aLong) {
        return null;
    }

    @Override
    public Follow getById(Long aLong) {
        return null;
    }

    @Override
    public Follow getReferenceById(Long aLong) {
        return null;
    }

    @Override
    public <S extends Follow> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Follow> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Follow> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Follow> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Follow> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Follow> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Follow, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public <S extends Follow> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<Follow> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public List<Follow> findAll() {
        return null;
    }

    @Override
    public List<Follow> findAllById(Iterable<Long> longs) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public void delete(Follow entity) {
        em.remove(entity);

    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends Follow> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<Follow> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Follow> findAll(Pageable pageable) {
        return null;
    }
}