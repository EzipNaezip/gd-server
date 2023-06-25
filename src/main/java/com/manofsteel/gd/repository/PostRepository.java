package com.manofsteel.gd.repository;

import com.manofsteel.gd.type.entity.Post;
import com.manofsteel.gd.type.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, QPostRepository {

    Optional<Post> findByPostNum(Long postNum);

    @Modifying
    @Query("UPDATE Post p SET p.likesCount = p.likesCount + 1 WHERE p.postNum = :postId")
    void addLikesCount(Post post);

    @Modifying
    @Query("UPDATE Post p SET p.likesCount = p.likesCount - 1 WHERE p.postNum = :postId")
    void subLikesCount(Post post);

    List<Post> findByWriterId(User writerId);

    List<Post> findByContentContaining(String content);

    List<Post> findTop30ByOrderByLikesCountDesc();

    Post save(Post post);

    boolean existsPostByWriterIdAndPostNum(User user, Long postNum);
}
