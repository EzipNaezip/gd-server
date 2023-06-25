package com.manofsteel.gd.type.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.manofsteel.gd.exception.ThumbnailCreationException;
import com.manofsteel.gd.exception.ThumbnailIOException;
import com.manofsteel.gd.util.ImageUtil;
import jakarta.persistence.*;
import lombok.*;
import net.coobird.thumbnailator.Thumbnails;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
@Table(name = "post", indexes = @Index(name = "idx_writer_id", columnList = "writer_id"))
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postNum;

    @Column(nullable = true)
    private String content;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "writer_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User writerId;

    @Column(columnDefinition = "TEXT")
    private String path;

    @Lob
    private String thumbnailImgUrl;

    private long likesCount;








}
