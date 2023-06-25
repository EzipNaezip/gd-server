package com.manofsteel.gd.type.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Example {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long serialNum;

    private String imgUrl;

    private String prompt;

}
