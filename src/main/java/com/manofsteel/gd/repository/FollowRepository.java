package com.manofsteel.gd.repository;

import com.manofsteel.gd.type.entity.Follow;
import com.manofsteel.gd.type.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {

    List<Follow> findByFollower(User user);

    Follow save(Follow follow);
    Optional<Follow> findByFollowerAndFollowing(User follower, User following);


    List<Follow> findByFollowing(User user);

    Boolean existsByFollowerIdAndFollowingId(Long followerId, Long followingId);

    void delete(Follow follow);
}