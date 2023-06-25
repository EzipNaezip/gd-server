package com.manofsteel.gd.type.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LikesRequestDto {

    private UserDto userId;
    private PostDto postNum;

    public LikesRequestDto(UserDto userId, PostDto postNum) {
        this.userId = userId;
        this.postNum = postNum;
    }

}
