package com.manofsteel.gd.type.vo;

import com.manofsteel.gd.type.entity.User;
import com.manofsteel.gd.type.etc.Role;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class UserVo {
    private Long userId;
    private String name;
    private String email;
    private String description;
    private String profileImgUrl;
    private Long postCount;
    private Long followerCount;
    private Long followCount;
    private Role role;
    private Boolean isMe;
    private Boolean isFollow;

    public UserVo(User user, Boolean isMe,Boolean isFollow) {
        this.userId = user.getUserId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.description = user.getDescription();
        this.profileImgUrl = user.getProfileImgUrl();
        this.postCount = user.getPostCount();
        this.followerCount = user.getFollowerCount();
        this.followCount = user.getFollowCount();
        this.role = user.getRole();
        this.isMe = isMe;
        this.isFollow = isFollow;
    }
}
