package com.manofsteel.gd.repository;


import com.manofsteel.gd.type.entity.Post;
import com.manofsteel.gd.type.entity.PostAndTagRelation;
import com.manofsteel.gd.type.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostAndTagRelationRepository extends JpaRepository<PostAndTagRelation, Long> {
     List<PostAndTagRelation> findAllByPostNum(Post post);


     List<PostAndTagRelation> findAllByTagId(Tag tags);

}
