package com.manofsteel.gd.type.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Bookmark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long serialNum;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User userId;

    @ManyToOne
    @JoinColumn(name = "post_num")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Post postNum;

    @Builder
    public Bookmark(User userId, Post postNum) {
        this.userId = userId;
        this.postNum = postNum;
    }

}
