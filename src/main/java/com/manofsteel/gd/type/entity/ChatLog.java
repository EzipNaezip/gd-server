package com.manofsteel.gd.type.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.annotation.Nullable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "chat_log", indexes = @Index(name = "idx_user_id", columnList = "user_id"))
public class ChatLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long serialNum;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User userId;

    @OneToOne
    @JoinColumn(name = "post_num")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Post postNum;

    @Column(nullable = true)
    private String contents;

    @Column(columnDefinition = "TEXT")
    private String message;

    private LocalDateTime timestamp;

    @Lob
    private List<String> tag;

    @OneToMany(mappedBy = "serialNum", cascade = CascadeType.ALL)
    private List<DalleImageItem> dalleImageItems;

}
