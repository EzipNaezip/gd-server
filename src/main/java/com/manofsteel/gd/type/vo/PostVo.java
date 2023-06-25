package com.manofsteel.gd.type.vo;

import jakarta.persistence.Lob;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
public class PostVo {
    private Long postNum;
    @Lob
    private String thumbnailImgUrl;
    @Builder.Default
    private boolean isBookmark = false;

    @Builder.Default
    private boolean isMe = false;

    public PostVo(Long postNum, String thumbnailImgUrl) {
        this.postNum = postNum;
        this.thumbnailImgUrl = thumbnailImgUrl;
    }

    public PostVo(Long postNum, String thumbnailImgUrl, boolean isBookmark,boolean isMe) {
        this.postNum = postNum;
        this.thumbnailImgUrl = thumbnailImgUrl;
        this.isBookmark = isBookmark;
        this.isMe=isMe;
    }
}
