package com.blog.api.repository;

import com.blog.api.entity.Like;
import com.blog.api.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

    Optional<Like> findByPostAndUserId(Post post, Long userId);

}
