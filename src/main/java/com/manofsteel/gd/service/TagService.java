package com.manofsteel.gd.service;


import com.manofsteel.gd.type.entity.*;
import com.manofsteel.gd.util.EntityUtil;
import jakarta.transaction.Transactional;
import com.manofsteel.gd.exception.NotFoundException;
import com.manofsteel.gd.repository.*;
import com.manofsteel.gd.type.dto.PostDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagService {

    private final PostRepository postRepository;
    private final TagRepository tagRepository;
    private final PostAndTagRelationRepository postAndTagRelationRepository;
    private final BookmarkRepository bookmarkRepository;

    private final UserRepository userRepository;


    @Transactional
    public List<PostDto> getPostsWithInfoByTag(String tagName, Long userId) {
        User user = EntityUtil.findUser(userRepository, userId);
        // 1. 주어진 태그 값이 DB에 있는지 조회
        Tag tags= EntityUtil.findTag(tagRepository, tagName);
        // 2. 태그에 해당하는 게시물과 북마크 정보를 가져옴
        List<PostAndTagRelation> postAndTagRelations = postAndTagRelationRepository.findAllByTagId(tags);
        List<PostDto> postList = new ArrayList<>();
        for (PostAndTagRelation postAndTag : postAndTagRelations) {
            Long postNum = postAndTag.getPostNum().getPostNum();
            // 3. 게시물 정보를 조회
           Post post = EntityUtil.findPost(postRepository, postNum);
            if (post != null) {
                // 4. 북마크 여부를 확인
                Boolean isBookmarked = bookmarkRepository.existsBookmarkByUserIdAndPostNum(user, post);
                PostDto postDto = PostDto.builder()
                        .postNum(post.getPostNum())
                        .content(post.getContent())
                        .writerId(post.getWriterId())
                        .thumbnailImgUrl(post.getThumbnailImgUrl())
                        .bookmark(isBookmarked)
                        .build();
                postList.add(postDto);
            }
        }
        return postList;
    }

    @Transactional
    public Tag insertTag(String keyword) {
        Tag tag = tagRepository.findByTagName(keyword).orElse(null);
        if (tag == null) {
            tag = Tag.builder()
                    .tagName(keyword)
                    .build();
            tagRepository.save(tag);
        }
        return tag;
    }

    @Transactional
    public void LinkPostAndTag(Tag tags, Post posts) {
       Post post= EntityUtil.findPost(postRepository, posts.getPostNum());
        Tag tag= EntityUtil.findTag(tagRepository, tags.getTagName());
        PostAndTagRelation postAndTagRelation = PostAndTagRelation.builder()
                .postNum(post)
                .tagId(tag)
                .build();
        postAndTagRelationRepository.save(postAndTagRelation);
    }

    @Transactional
    public List<Tag> getTagsByPost(Long postNum) {
      Post post = EntityUtil.findPost(postRepository, postNum);
        List<Tag> tagList;
        try {
            tagList = tagRepository.findAll();
            List<Tag> postTags = new ArrayList<>();
            int i=0;
            for (Tag tag : tagList) {
                if(i==3)
                    break;
                //게시물의 내용에 태그가 포함되어있는지 확인
                if (post.getContent().contains(tag.getTagName())) {
                    LinkPostAndTag(tag, post);
                    postTags.add(tag);
                    i++;
                }


            }
            return postTags;


        } catch (Exception e) {
            return null;
        }


    }

    @Transactional
    public List<Tag> getTagsByContent(String content) {
//태그 목록을 가져온다. 없다면 null 반환
        List<Tag> tagList;
        try {
            tagList = tagRepository.findAll();
            if (tagList.isEmpty()) {
                throw new NotFoundException("저장된 태그가 없습니다.");
            }
            else{
                List<Tag> postTags = new ArrayList<>();
                int i=0;
                for (Tag tag : tagList) {
                    if(i==3)
                        break;
                    //게시물의 내용에 태그가 포함되어있는지 확인
                    if (content.contains(tag.getTagName())) {
                        postTags.add(tag);
                        i++;
                    }
                }
                return postTags;
            }


        } catch (Exception e) {
             throw new NotFoundException("저장된 태그가 없습니다.");
        }

//contents 를 keywords에 이어 붙인다.


    }

}