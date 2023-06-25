package com.manofsteel.gd.type.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Data
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long serialNum;

    @ManyToOne
    @JoinColumn(name = "follower_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User follower;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "following_id")
    private User following;

}
