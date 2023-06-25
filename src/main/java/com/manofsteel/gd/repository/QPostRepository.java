package com.manofsteel.gd.repository;

import com.manofsteel.gd.type.entity.Post;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QPostRepository {

    List<Post> findAll(Long start, Long display);

}
