package com.manofsteel.gd.type.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FollowDto {


    private Long userId;
    private String name;
    private String profileImgUrl;
    private boolean isFollow;



}
