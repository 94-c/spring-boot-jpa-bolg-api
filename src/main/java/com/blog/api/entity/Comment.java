package com.blog.api.entity;

import com.blog.api.entity.common.LocalDate;
import lombok.*;

import javax.persistence.*;

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
    private Integer parentId;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "post_id", foreignKey = @ForeignKey(name = "FK_user_comment"))
    private Post post;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "FK_post_comment"))
    private User user;

    @Embedded
    private LocalDate date;

    public void mappingPostAndUser(Post post, User user) {
        this.post = post;
        this.user = user;

        post.mappingComment(this);
        user.mappingComment(this);
    }

    public void changeContent(String content) {
        this.content = content;
    }
}
