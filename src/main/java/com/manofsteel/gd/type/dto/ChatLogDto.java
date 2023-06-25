package com.manofsteel.gd.type.dto;

import com.manofsteel.gd.type.entity.ChatLog;
import com.manofsteel.gd.type.entity.DalleImageItem;
import com.manofsteel.gd.type.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ChatLogDto {
    private Long serialNum;
    private User userId;
    private String message;

    private String contents;
    private List<byte[]> imgUrl;

    private LocalDateTime timestamp=LocalDateTime.now();
    private List<String> tag;

    private List<DalleImageItem> url;

    private Long postNum;







    public ChatLog toEntity() {
        return ChatLog.builder()
                .serialNum(serialNum)
                .userId(userId)
                .message(message)
                .contents(contents)
                .timestamp(timestamp)
                .tag(tag)


                .build();
    }
}
