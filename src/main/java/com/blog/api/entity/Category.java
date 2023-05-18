package com.blog.api.entity;

import com.blog.api.entity.common.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "categorys")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    @Column(unique = true)
    private String value;

    @OneToMany(mappedBy = "category")
    private List<Post> posts = new ArrayList<>();

    @Embedded
    private LocalDate date;

    public void mappingPost(Post post) {
        this.posts.add(post);
    }

}
