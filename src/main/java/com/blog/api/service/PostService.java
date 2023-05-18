package com.blog.api.service;

import com.blog.api.dto.PostDto;
import com.blog.api.entity.Post;
import com.blog.api.exception.PostNotFoundException;
import com.blog.api.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final PostRepository postRepository;

    public PostDto createPost(PostDto dto) {
        Post post = Post.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .createdAt(LocalDateTime.now())
                .build();

        Post createPost = postRepository.save(post);

        PostDto postDtoResponse = PostDto.builder()
                .id(createPost.getId())
                .build();

        return postDtoResponse;
    }


    public PostDto getPost(Long postId) {
        Optional<Post> findById = postRepository.findById(postId);

        Post findPost = findById.orElseThrow(() -> new PostNotFoundException("해당 포스트가 존재하지 않습니다."));

        return PostDto.builder()
                .id(findPost.getId())
                .title(findPost.getTitle())
                .content(findPost.getContent())
                .createdAt(findPost.getCreatedAt().toString())
                .build();
    }

    public PostDto updatePost(Long postId, PostDto dto) {
        Optional<Post> byId = postRepository.findById(postId);
        Post post = byId.orElseThrow(() -> new PostNotFoundException("해당 포스트가 존재하지 않습니다."));

        post.changeTitle(dto.getTitle());
        post.changeContent(dto.getContent());

        return PostDto.builder()
                .id(post.getId())
                .build();
    }

    public void deletePost(Long postId) {
        Optional<Post> byId = postRepository.findById(postId);
        Post post = byId.orElseThrow(() -> new PostNotFoundException("해당 포스트가 존재하지 않습니다."));

        postRepository.delete(post);
    }
}

