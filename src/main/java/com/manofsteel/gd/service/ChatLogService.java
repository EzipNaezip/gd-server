package com.manofsteel.gd.service;

import com.manofsteel.gd.exception.NotFoundException;
import com.manofsteel.gd.exception.PermissionDeniedException;
import com.manofsteel.gd.repository.ChatLogRepository;
import com.manofsteel.gd.repository.DalleImageRepository;
import com.manofsteel.gd.repository.PostRepository;
import com.manofsteel.gd.repository.UserRepository;
import com.manofsteel.gd.type.dto.ChatLogDto;
import com.manofsteel.gd.type.dto.ChatLogRequestDto;
import com.manofsteel.gd.type.entity.ChatLog;
import com.manofsteel.gd.type.entity.DalleImageItem;
import com.manofsteel.gd.type.entity.User;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ChatLogService {
    private final ChatLogRepository chatLogRepository;
    private final UserRepository userRepository;

    private final DalleImageRepository dalleImageRepository;

    private final PostRepository postRepository;


    private final PostService postService;

    @Transactional
    public ChatLog save(ChatLogDto dto, Long userId) {
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new IllegalArgumentException("로그인 후 이용해주세요"));
        dto.setUserId(user);
        ChatLog chatLog = dto.toEntity();
        chatLogRepository.save(chatLog);
        return chatLog;

    }
    @Transactional
    public void delete(Long serialNum,User user) throws PermissionDeniedException {

        ChatLog chatLog=chatLogRepository.findBySerialNum(serialNum).orElseThrow(() -> new NotFoundException("해당 chatLog를 찾지 못했습니다."));
        Boolean isMe = chatLogRepository.existsChatLogByUserIdAndSerialNum(user,serialNum);
        if(isMe=false){
            throw new PermissionDeniedException("해당 유저가 만든 챗로그가 아닙니다.");
        }
        postService.delete(chatLog.getPostNum().getPostNum(),user.getUserId());

    }

}
