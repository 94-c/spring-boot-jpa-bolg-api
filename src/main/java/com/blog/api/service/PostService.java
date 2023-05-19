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
import com.blog.api.util.resource.PostResource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    //TODO 검색 기능 사용 예정
    @Transactional(readOnly = true)
    public PostResource findAllPosts(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Post> posts = postRepository.findAll(pageable);

        List<Post> listOfPosts = posts.getContent();

        List<PostDto> content = listOfPosts.stream().map(PostDto::convertToPostDTO).collect(Collectors.toList());

        PostResource postResource = new PostResource();

        postResource.setContent(content);
        postResource.setPageNo(pageNo);
        postResource.setPageSize(pageSize);
        postResource.setTotalElements(posts.getTotalElements());
        postResource.setTotalPages(posts.getTotalPages());
        postResource.setLast(postResource.isLast());

        return postResource;
    }

    public PostDto createPost(PostDto dto, String email) {
        User findUser = getUserInfo(email);

        Post post = Post.builder()
                .user(findUser)
                .title(dto.getTitle())
                .content(dto.getContent())
                .date(LocalDate.builder()
                        .createdAt(LocalDateTime.now())
                        .build())
                .build();

        Post createPost = postRepository.save(post);

        return PostDto.builder()
                .id(createPost.getId())
                .title(createPost.getTitle())
                .content(createPost.getContent())
                .createdAt(createPost.getDate().getCreatedAt())
                .build();
    }

    public PostDto getPost(Long postId) {
        Optional<Post> findById = postRepository.findById(postId);

        Post findPost = findById.orElseThrow(() -> new PostNotFoundException("해당 포스트가 존재하지 않습니다."));

        List<CommentDto> commentDtos = CommentDto.convertToCommentDtoList(findPost.getComments());

        return PostDto.builder()
                .id(findPost.getId())
                .user(UserDto.convertToUserDTO(findPost.getUser()))
                .title(findPost.getTitle())
                .content(findPost.getContent())
                .commentList(commentDtos)
                .createdAt(findPost.getDate().getCreatedAt())
                .build();
    }

    public PostDto updatePost(Long postId, PostDto dto) {
        Optional<Post> findPost = postRepository.findById(postId);

        Post post = findPost.orElseThrow(() -> new PostNotFoundException("해당 포스트가 존재하지 않습니다."));

        post.changeTitle(dto.getTitle());
        post.changeContent(dto.getContent());
        post.getDate().changeUpdateAt(LocalDateTime.now());

        Post updatePost = postRepository.save(post);

        return PostDto.builder()
                .id(updatePost.getId())
                .user(UserDto.convertToUserDTO(updatePost.getUser()))
                .title(updatePost.getTitle())
                .content(updatePost.getContent())
                .createdAt(updatePost.getDate().getCreatedAt())
                .updatedAt(updatePost.getDate().getUpdateAt())
                .build();
    }

    public void deletePost(Long postId) {
        Optional<Post> byId = postRepository.findById(postId);
        Post post = byId.orElseThrow(() -> new PostNotFoundException("해당 포스트가 존재하지 않습니다."));

        postRepository.delete(post);
    }
}

