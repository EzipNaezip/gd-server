package com.manofsteel.gd.repository;

import com.manofsteel.gd.type.entity.Bookmark;
import com.manofsteel.gd.type.entity.Post;
import com.manofsteel.gd.type.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    Optional<Bookmark> findByUserIdAndPostNum(User userId, Post postNum);

    boolean existsBookmarkByUserIdAndPostNum(User userId, Post postNum);

    List<Post> findByUserId(User userId);

    List<Bookmark> findBookmarkByUserId(User userId);


}
