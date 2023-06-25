package com.manofsteel.gd.type.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "comment", indexes = {
        @Index(name = "idx_writer_id", columnList = "writer_id"),
        @Index(name = "idx_post_num", columnList = "post_num")
})
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long serialNum;

    @Column(nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "writer_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User writerId;

    @ManyToOne
    @JoinColumn(name = "post_num")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Post postNum;

    public void update(String content) {
        this.content = content;
    }

}
