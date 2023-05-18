package com.blog.api.service;

import com.blog.api.dto.CommentDto;
import com.blog.api.entity.Comment;
import com.blog.api.entity.Post;
import com.blog.api.entity.User;
import com.blog.api.entity.common.LocalDate;
import com.blog.api.exception.PostNotFoundException;
import com.blog.api.exception.UserNotFoundException;
import com.blog.api.repository.CommentRepository;
import com.blog.api.repository.PostRepository;
import com.blog.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public CommentDto createComment(Long postId, CommentDto dto, String email) {
        Optional<Post> findByPost = postRepository.findById(postId);

        Post post = findByPost.orElseThrow(() -> new PostNotFoundException("게시물이 삭제 되었거나 존재하지 않습니다."));

        Optional<User> findByUser = userRepository.findByEmail(email);

        User user = findByUser.orElseThrow(() -> new UserNotFoundException("유저를 찾을 수 없습니다."));

        Comment comment = Comment.builder()
                .content(dto.getContent())
                .date(LocalDate.builder()
                        .createdAt(LocalDateTime.now())
                        .build())
                .build();
        comment.mappingPostAndUser(post, user);

        Comment createComment = commentRepository.save(comment);

        return CommentDto.converToCommentDto(createComment);
    }


}
