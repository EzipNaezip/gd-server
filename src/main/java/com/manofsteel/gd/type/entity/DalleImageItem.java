package com.manofsteel.gd.type.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.annotation.Nullable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DalleImageItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemId;

    @Column(columnDefinition = "TEXT")
    private String url;

    @ManyToOne
    @JoinColumn(name = "serialNum")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ChatLog serialNum;
    //파일 경로

    @Column(columnDefinition = "TEXT")
   private String path;

}