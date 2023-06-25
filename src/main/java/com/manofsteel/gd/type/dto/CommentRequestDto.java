package com.manofsteel.gd.type.dto;

import com.manofsteel.gd.type.entity.Post;
import com.manofsteel.gd.type.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentRequestDto {
    private String content;

    private Long postNum;
    }