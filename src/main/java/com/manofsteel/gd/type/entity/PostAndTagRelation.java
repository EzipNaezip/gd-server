package com.manofsteel.gd.type.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class PostAndTagRelation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long serialNum;

    @ManyToOne
    @JoinColumn(name = "post_num")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Post postNum;

    @ManyToOne
    @JoinColumn(name = "tag_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Tag tagId;

}
