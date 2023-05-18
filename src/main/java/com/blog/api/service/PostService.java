package com.blog.api.service;

import com.blog.api.dto.*;
import com.blog.api.entity.Category;
import com.blog.api.entity.Post;
import com.blog.api.entity.User;
import com.blog.api.entity.common.LocalDate;
import com.blog.api.exception.PostNotFoundException;
import com.blog.api.repository.CategoryRepository;
import com.blog.api.repository.PostRepository;
import com.blog.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    @Transactional(readOnly = true)
    public List<PostDto> findAllPostList(SearchDto searchDto) {
        Stream<Post> stream = postRepository.postListQueryDSL(searchDto).stream();

        return stream.map(PostDto::convertToPostDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<PostDto> getPostsList(SearchDto searchDto, Pageable pageable) {
        Page<Post> posts = postRepository.postListPagingQueryDSL(searchDto, pageable);

        return posts.map(PostDto::convertToPostDTO);
    }


    public PostDto createPost(PostDto dto, String email) {
        User user = getUserInfo(email);

        Post post = Post.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .date(LocalDate.builder()
                        .createdAt(LocalDateTime.now())
                        .build())
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
        List<CommentDto> commentDtos = CommentDto.convertToCommentDtoList(findPost.getComments());

        return PostDto.builder()
                .id(findPost.getId())
                .category(findPost.getCategory().getName())
                .user(UserDto.convertToUserDTO(findPost.getUser()))
                .title(findPost.getTitle())
                .content(findPost.getContent())
                .commentList(commentDtos)
                .createdAt(findPost.getDate().toString())
                .build();
    }

    public PostDto updatePost(Long postId, PostDto dto) {
        Optional<Post> findById = postRepository.findById(postId);

        Post post = findById.orElseThrow(() -> new PostNotFoundException("해당 포스트가 존재하지 않습니다."));

        post.changeTitle(dto.getTitle());
        post.changeContent(dto.getContent());
        post.getDate().changeUpdateAt(LocalDateTime.now());

        return PostDto.builder()
                .id(post.getId())
                .build();
    }

    /*
        TODO 각 서비스별 CRUD 권한인 principal에 대하여 적용 해야함
     */
    public void deletePost(Long postId) {
        Optional<Post> byId = postRepository.findById(postId);
        Post post = byId.orElseThrow(() -> new PostNotFoundException("해당 포스트가 존재하지 않습니다."));

        postRepository.delete(post);
    }
}

