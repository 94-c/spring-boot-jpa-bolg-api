package com.blog.api.service;

import com.blog.api.dto.CommentDto;
import com.blog.api.dto.UserDto;
import com.blog.api.entity.Comment;
import com.blog.api.entity.Post;
import com.blog.api.entity.User;
import com.blog.api.entity.common.LocalDate;
import com.blog.api.exception.NotFoundException;
import com.blog.api.repository.CommentRepository;
import com.blog.api.repository.PostRepository;
import com.blog.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    private User getUserInfo(String email) {
        Optional<User> byEmail = userRepository.findByEmail(email);

        return byEmail.orElseThrow(() -> new NotFoundException(404, "유저가 없습니다."));
    }


    public CommentDto createComment(Long postId, CommentDto dto, String email) {
        Optional<Post> findByPost = postRepository.findById(postId);

        Post post = findByPost.orElseThrow(() -> new NotFoundException(404, "게시물을 찾을 수 없습니다."));

        User user = getUserInfo(email);

        Comment comment = Comment.builder()
                .content(dto.getContent())
                .userId(user.getId())
                .date(LocalDate.builder()
                        .createdAt(LocalDateTime.now())
                        .build())
                .build();
        comment.mappingPost(post);

        Comment createComment = commentRepository.save(comment);

        return CommentDto.convertToCommentDto(createComment);
    }

    public CommentDto getComment(Long postId, Long commentId) {
        Optional<Post> findByPost = postRepository.findById(postId);

        findByPost.orElseThrow(() -> new NotFoundException(404, "해당 포스트가 존재하지 않습니다."));

        Optional<Comment> findByComment = commentRepository.findById(commentId);

        Comment comment = findByComment.orElseThrow(() -> new NotFoundException(404, "해당 댓글 존재하지 않습니다."));

        //commentRepository에서 parent_id 값으로 select 할 수 있도록 코드 작성
        List<Comment> subComment = commentRepository.findBy(comment.getId());

        return CommentDto.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .userId(comment.getUserId())
                .createdAt(comment.getDate().getCreatedAt())
                .updatedAt(comment.getDate().getUpdateAt())
                .subCommentDtoList()
                .build();
    }

    public CommentDto updateComment(Long postId, Long commentId, CommentDto dto) {
        Optional<Post> findByPost = postRepository.findById(postId);

        findByPost.orElseThrow(() -> new NotFoundException(404, "해당 포스트가 존재하지 않습니다."));

        Optional<Comment> findByComment = commentRepository.findById(commentId);

        Comment comment = findByComment.orElseThrow(() -> new NotFoundException(404, "해당 댓글 존재하지 않습니다."));

        comment.changeContent(dto.getContent());
        comment.getDate().changeUpdateAt(LocalDateTime.now());

        Comment updateComment = commentRepository.save(comment);

        return CommentDto.builder()
                .id(updateComment.getId())
                .content(updateComment.getContent())
                .userId(updateComment.getUserId())
                .updatedAt(updateComment.getDate().getUpdateAt())
                .build();
    }

    public void deleteComment(Long postId, Long commentId) {
        Optional<Post> findByPost = postRepository.findById(postId);

        findByPost.orElseThrow(() -> new NotFoundException(404, "해당 포스트가 존재하지 않습니다."));

        Optional<Comment> findByCommentId = commentRepository.findById(commentId);

        Comment comment = findByCommentId.orElseThrow(() -> new NotFoundException(404, "해당 댓글 존재하지 않습니다."));

        commentRepository.delete(comment);
    }

}
