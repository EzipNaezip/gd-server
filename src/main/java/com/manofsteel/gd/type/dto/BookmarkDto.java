package com.manofsteel.gd.type.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BookmarkDto {

    private Long serialNum;
    private UserDto userId;
    private PostDto postNum;
}
