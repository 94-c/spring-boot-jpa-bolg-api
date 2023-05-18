package com.blog.api.repository;

import com.blog.api.entity.Post;
import com.blog.api.repository.query.PostQueryRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, PostQueryRepository {

    @EntityGraph(attributePaths = {"category", "user"})
    @Override
    Optional<Post> findById(Long postId);

    @EntityGraph(attributePaths = {"user", "category"})
    @Override
    List<Post> findAll(Sort sort);

}
