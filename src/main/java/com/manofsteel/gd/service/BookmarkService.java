package com.manofsteel.gd.service;

import com.manofsteel.gd.exception.DuplicateInsertionException;
import com.manofsteel.gd.exception.NotFoundException;
import com.manofsteel.gd.repository.BookmarkRepository;
import com.manofsteel.gd.repository.PostRepository;
import com.manofsteel.gd.type.entity.Bookmark;
import com.manofsteel.gd.type.entity.Post;
import com.manofsteel.gd.type.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final PostRepository postRepository;
    private final BookmarkRepository bookmarkRepository;

    public void bookmarkPost(Long postNum, User user) {
        Post post = postRepository.findByPostNum(postNum)
                .orElseThrow(() -> new NotFoundException("Could not find Post : " + postNum));

        if(bookmarkRepository.existsBookmarkByUserIdAndPostNum(user, post)){
            throw new DuplicateInsertionException("This Bookmark is Duplicated.");
        }

        Bookmark bookmark = Bookmark.builder()
                .userId(user)
                .postNum(post)
                .build();

        bookmarkRepository.save(bookmark);
    }

    public void unbookmarkPost(Long postNum, User user) {
        Post post = postRepository.findByPostNum(postNum)
                .orElseThrow(() -> new NotFoundException("Could not find Post : " + postNum));
        Bookmark bookmark = bookmarkRepository.findByUserIdAndPostNum(user, post)
                .orElseThrow(() -> new NotFoundException("Could not find Bookmark"));
        bookmarkRepository.delete(bookmark);
    }

}
