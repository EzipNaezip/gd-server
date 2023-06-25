package com.manofsteel.gd.type.dto;

import com.manofsteel.gd.type.entity.ChatLog;
import com.manofsteel.gd.type.entity.User;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatLogRequestDto {
    private Long serialNum;
    private User user;
    private String message;
    private LocalDateTime timestamp;

    private List<String> tag;
    
    public ChatLog toEntity(){
        ChatLog chatLog=ChatLog.builder()
                .userId(user)
                .message(message)
                .timestamp(timestamp)

                .tag(tag)
                .build();
        return chatLog;
    }


}
