package com.blog.api.entity;

import com.blog.api.entity.common.LocalDate;
import lombok.*;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @Column(name = "parent_id")
    private Long parentId;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Column(name = "user_id")
    private Long userId;

    @Embedded
    private LocalDate date;

    public void mappingPost(Post post) {
        this.post = post;

        post.mappingComment(this);
    }

    public void changeContent(String content) {
        this.content = content;
    }

}
