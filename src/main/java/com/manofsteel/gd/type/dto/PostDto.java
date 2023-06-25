package com.manofsteel.gd.type.dto;

import com.manofsteel.gd.type.entity.Comment;
import com.manofsteel.gd.type.entity.Post;
import com.manofsteel.gd.type.entity.User;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class PostDto {
    private Long postNum;
    private String content;
    private LocalDateTime timestamp;
    private User writerId;
    private String thumbnailImgUrl;
    private long likesCount;
    private String path;
    private Boolean bookmark;
    private Boolean like;
    private Boolean follow;
    private Boolean isMe;
    private String profileImgUrl;
    private List<Long> tagIds;

    private String description;
    public Post toEntity()
    {
        return Post.builder()
                .postNum(postNum)
                .content(content)
                .timestamp(timestamp)
                .writerId(writerId)
                .thumbnailImgUrl(thumbnailImgUrl)
                .likesCount(likesCount)
                .path(path)
                .build();
    }
}
