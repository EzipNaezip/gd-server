package com.manofsteel.gd.repository;

import com.manofsteel.gd.type.entity.Comment;
import com.manofsteel.gd.type.entity.Post;
import com.manofsteel.gd.type.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>,QCommentRepository {
    Optional<Comment> findBySerialNum(Long serialNum);

    List<Comment> findAllByPostNum(Post postNum);

    Comment save(Comment comment);

    boolean existsCommentByWriterIdAndSerialNum(User user, Long serialNum);

}
