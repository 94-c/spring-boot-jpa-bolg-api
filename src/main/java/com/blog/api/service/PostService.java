package com.blog.api.service;

import com.blog.api.dto.PostDto;
import com.blog.api.entity.Post;
import com.blog.api.entity.User;
import com.blog.api.exception.PostNotFoundException;
import com.blog.api.repository.CategoryRepository;
import com.blog.api.repository.PostRepository;
import com.blog.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    private User getUserInfo(String email) {
        Optional<User> byEmail = userRepository.findByEmail(email);

        User user = byEmail.orElseThrow(() -> new UsernameNotFoundException("게시글 작성 권한이 없습니다."));

        return user;
    }
    public PostDto createPost(PostDto dto, String email) {
        User user = getUserInfo(email);

        Post post = Post.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .createdAt(LocalDateTime.now())
                .build();

        post.mappingUser(user);
        post.mappingCategory(categoryRepository.findByName(dto.getCategory()));

        Post createPost = postRepository.save(post);

        return PostDto.builder()
                .id(createPost.getId())
                .build();
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

