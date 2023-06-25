package com.manofsteel.gd.type.dto;

import com.manofsteel.gd.type.etc.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class UserDto {
    private Long userId;
    private String name;
    //private String email;
    private String description;
    private String profileImgUrl;
   // private Long postCount;
   // private Long followerCount;
   // private Long followCount;
   // private Role role;
}
