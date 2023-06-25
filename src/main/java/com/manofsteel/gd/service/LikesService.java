package com.manofsteel.gd.service;

import com.manofsteel.gd.exception.DuplicateInsertionException;
import com.manofsteel.gd.exception.NotFoundException;
import com.manofsteel.gd.repository.LikesRepository;
import com.manofsteel.gd.repository.PostRepository;
import com.manofsteel.gd.type.entity.Likes;
import com.manofsteel.gd.type.entity.Post;
import com.manofsteel.gd.type.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikesService {

    private final PostRepository postRepository;
    private final LikesRepository likesRepository;

    public void likePost(Long postNum, User user) {
        Post post = postRepository.findByPostNum(postNum)
                .orElseThrow(() -> new NotFoundException("Could not find Post : " + postNum));

        if(likesRepository.existsLikesByUserIdAndPostNum(user, post)){
            throw new DuplicateInsertionException("This Like is Duplicated.");
        }

        Likes likes = Likes.builder()
                .userId(user)
                .postNum(post)
                .build();


        likesRepository.save(likes);
        post.setLikesCount(post.getLikesCount()+1);
        postRepository.save(post);
    }

    public void unlike(Long postNum, User user) {
        Post post = postRepository.findByPostNum(postNum)
                .orElseThrow(() -> new NotFoundException("Could not find Post : " + postNum));
        Likes likes = likesRepository.findByUserIdAndPostNum(user, post)
                .orElseThrow(() -> new NotFoundException("Could not find Likes"));

        likesRepository.delete(likes);
        post.setLikesCount(post.getLikesCount()-1);
        postRepository.save(post);
    }
}
