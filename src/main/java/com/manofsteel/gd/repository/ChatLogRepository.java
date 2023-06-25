package com.manofsteel.gd.repository;

import com.manofsteel.gd.type.entity.ChatLog;
import com.manofsteel.gd.type.entity.Post;
import com.manofsteel.gd.type.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface ChatLogRepository extends JpaRepository<ChatLog, Long> {

    List<ChatLog> findByUserId(User user);


    // save comment
    ChatLog save(ChatLog chatLog);

    Optional<ChatLog> findBySerialNum(Long serialNum);


    Optional<ChatLog> findByPostNum(Post postNum);

    Boolean existsChatLogByUserIdAndSerialNum(User userId, Long serialNum);
}
