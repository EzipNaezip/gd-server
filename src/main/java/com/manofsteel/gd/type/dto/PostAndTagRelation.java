package com.manofsteel.gd.type.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostAndTagRelation {
    private Long serialNum;
    private PostDto postNum;
    private TagDto tagId;
}
