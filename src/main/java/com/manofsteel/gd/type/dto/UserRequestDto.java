package com.manofsteel.gd.type.dto;

import com.manofsteel.gd.type.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequestDto {
    private Long userId;
    private String name;
    private String description;
    private String profileImgUrl;
    private Long postCount;
    private Long followerCount;
    private Long followCount;

public User toEntity(){
    User user=User.builder()
            .userId(userId)
            .description(description)
            .profileImgUrl(profileImgUrl)
            .postCount(postCount)
            .followerCount(followerCount)
            .followCount(followCount)
            .build();
    return user;
    }
}


