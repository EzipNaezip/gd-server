package com.manofsteel.gd.repository;

import com.manofsteel.gd.type.entity.Likes;
import com.manofsteel.gd.type.entity.Post;
import com.manofsteel.gd.type.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikesRepository extends JpaRepository<Likes, Long> {

    Optional<Likes> findByUserIdAndPostNum(User userId, Post postNum);

    boolean existsLikesByUserIdAndPostNum(User userId, Post postNum);

    List<Likes> findLikesByUserId(User userId);
}
