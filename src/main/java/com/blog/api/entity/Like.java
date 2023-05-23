package com.blog.api.entity;

import com.blog.api.entity.common.LocalDate;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "likes")
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    private Long userId;

    @Column(nullable = false)
    private boolean status; // true = 좋아요, false = 좋아요 취소

    @Embedded
    private LocalDate date;

}
