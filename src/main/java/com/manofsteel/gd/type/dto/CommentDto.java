package com.manofsteel.gd.type.dto;

import com.manofsteel.gd.type.entity.Comment;
import com.manofsteel.gd.type.entity.Post;
import com.manofsteel.gd.type.entity.User;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentDto {
    private Long serialNum;
    private User user;
    private String content;

private Boolean isMe;

    public Comment toEntity(){
    Comment comment=Comment.builder()
            .serialNum(serialNum)
            .content(content)
            .writerId(user)

            .build();
    return comment;
    }

}
