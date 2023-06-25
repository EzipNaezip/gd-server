package com.manofsteel.gd.util;

import com.manofsteel.gd.exception.NotFoundException;
import com.manofsteel.gd.repository.*;
import com.manofsteel.gd.type.entity.*;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class EntityUtil {

    private final UserRepository userRepository;
    private final ChatLogRepository chatLogRepository;

    public static User findUser(UserRepository userRepository, Long userId) {
        return userRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("해당 유저가 없습니다. id=" + userId));
    }
    public static Post findPost(PostRepository postRepository,Long postNum) {
        return postRepository.findByPostNum(postNum)
                .orElseThrow(() -> new NotFoundException("해당 게시글이 없습니다. id=" + postNum));
    }
    public static Comment findComment(CommentRepository commentRepository,Long serialNum) {
        return commentRepository.findBySerialNum(serialNum)
                .orElseThrow(() -> new NotFoundException("해당 댓글이 없습니다. id=" + serialNum));
    }
    public static Tag findTag(TagRepository tagRepository, String tagName) {
        return tagRepository.findByTagName(tagName)
                .orElseThrow(() -> new NotFoundException("해당 태그가 없습니다.  tagName=" +  tagName));
    }

   public static ChatLog findChatLog(ChatLogRepository chatLogRepository, Post postNum) {
        return chatLogRepository.findByPostNum(postNum)
                .orElseThrow(() -> new NotFoundException("해당 채팅방이 없습니다. postNum=" + postNum));
    }

    public static List<PostAndTagRelation> findPostAndTag(PostAndTagRelationRepository postAndTagRelationRepository, Post post) {
        return postAndTagRelationRepository.findAllByPostNum(post);
    }
}
