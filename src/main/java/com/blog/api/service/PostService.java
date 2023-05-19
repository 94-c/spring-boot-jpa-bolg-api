package com.blog.api.service;

import com.blog.api.dto.*;
import com.blog.api.entity.Post;
import com.blog.api.entity.User;
import com.blog.api.entity.common.LocalDate;
import com.blog.api.exception.NotFoundException;
import com.blog.api.repository.CategoryRepository;
import com.blog.api.repository.PostRepository;
import com.blog.api.repository.UserRepository;
import com.blog.api.util.resource.PageResource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    private User getUserInfo(String email) {
        Optional<User> byEmail = userRepository.findByEmail(email);

        return byEmail.orElseThrow(() -> new NotFoundException(404, "유저가 없습니다."));
    }

    //TODO 검색 기능 사용 예정
    @Transactional(readOnly = true)
    public PageResource<PostDto> findAllPosts(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Post> posts = postRepository.findAll(pageable);

        List<Post> listOfPosts = posts.getContent();

        List<PostDto> content = listOfPosts.stream().map(PostDto::convertToPostDto).collect(Collectors.toList());

        PageResource<PostDto> pageResource = new PageResource<>();

        pageResource.setContent(content);
        pageResource.setPageNo(pageNo);
        pageResource.setPageSize(pageSize);
        pageResource.setTotalElements(posts.getTotalElements());
        pageResource.setTotalPages(posts.getTotalPages());
        pageResource.setLast(pageResource.isLast());

        return pageResource;
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
                .user(UserDto.convertToUserDTO(createPost.getUser()))
                .title(createPost.getTitle())
                .content(createPost.getContent())
                .createdAt(createPost.getDate().getCreatedAt())
                .build();
    }

    public PostDto getPost(Long postId) {
        Optional<Post> findByPostId = postRepository.findById(postId);

        Post findPost = findByPostId.orElseThrow(() -> new NotFoundException(404, "해당 포스트가 존재하지 않습니다."));

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
        Optional<Post> findByPostId = postRepository.findById(postId);

        Post post = findByPostId.orElseThrow(() -> new NotFoundException(404, "해당 포스트가 존재하지 않습니다."));

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
        Optional<Post> findByPostId = postRepository.findById(postId);

        Post post = findByPostId.orElseThrow(() -> new NotFoundException(404, "해당 포스트가 존재하지 않습니다."));

        postRepository.delete(post);
    }
}

