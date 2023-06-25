package com.manofsteel.gd.type.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.manofsteel.gd.type.etc.OAuthProvider;
import com.manofsteel.gd.type.etc.Role;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userId;

    @Column(nullable = false)
    private String name;

    private String email;

    private String description;

    private String profileImgUrl;

    private Long postCount;

    private Long followerCount;

    private Long followCount;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private OAuthProvider provider;



    public User(Long userId) {
        this.userId = userId;
    }

}
