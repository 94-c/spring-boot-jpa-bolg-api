package com.blog.api.service;

import com.blog.api.dto.CommentDto;
import com.blog.api.entity.Comment;
import com.blog.api.entity.Post;
import com.blog.api.entity.User;
import com.blog.api.entity.common.LocalDate;
import com.blog.api.exception.NotFoundException;
import com.blog.api.repository.CommentRepository;
import com.blog.api.repository.PostRepository;
import com.blog.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubCommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    private User getUserInfo(String email) {
        Optional<User> byEmail = userRepository.findByEmail(email);

        return byEmail.orElseThrow(() -> new NotFoundException(404, "유저가 없습니다."));
    }


    public CommentDto createSubComment(Long postId, Long commentId, CommentDto dto, String email) {
        Optional<Post> findByPost = postRepository.findById(postId);

        Post post = findByPost.orElseThrow(() -> new NotFoundException(404, "게시물을 찾을 수 없습니다."));

        User user = getUserInfo(email);

        Comment comment = Comment.builder()
                .content(dto.getContent())
                .userId(user.getId())
                .parentId(commentId)
                .date(LocalDate.builder()
                        .createdAt(LocalDateTime.now())
                        .build())
                .build();

        comment.mappingPost(post);

        Comment createComment = commentRepository.save(comment);

        return CommentDto.convertToSubCommentDto(createComment);
    }

    public CommentDto getSubComment(Long postId, Long commentId, Long parentId) {
        Optional<Post> findByPost = postRepository.findById(postId);

        findByPost.orElseThrow(() -> new NotFoundException(404, "해당 포스트가 존재하지 않습니다."));

        Optional<Comment> findBySubComment = commentRepository.findByIdAndParentId(commentId, parentId);

        Comment subComment = findBySubComment.orElseThrow(() -> new NotFoundException(404, "해당 댓글 존재하지 않습니다."));

        return CommentDto.builder()
                .id(subComment.getId())
                .parentId(subComment.getParentId())
                .content(subComment.getContent())
                .userId(subComment.getUserId())
                .createdAt(subComment.getDate().getCreatedAt())
                .updatedAt(subComment.getDate().getUpdateAt())
                .build();
    }

    public CommentDto updateSubComment(Long postId, Long commentId, Long parentId, CommentDto dto) {
        Optional<Post> findByPost = postRepository.findById(postId);

        findByPost.orElseThrow(() -> new NotFoundException(404, "해당 포스트가 존재하지 않습니다."));

        Optional<Comment> findByComment = commentRepository.findByIdAndParentId(commentId, parentId);

        Comment subComment = findByComment.orElseThrow(() -> new NotFoundException(404, "해당 대댓글 존재하지 않습니다."));

        subComment.changeContent(dto.getContent());
        subComment.getDate().changeUpdateAt(LocalDateTime.now());

        Comment updateSubComment = commentRepository.save(subComment);

        return CommentDto.convertToSubCommentDto(updateSubComment);
    }

    public void deleteSubComment(Long postId, Long commentId, Long parentId) {
        Optional<Post> findByPost = postRepository.findById(postId);

        findByPost.orElseThrow(() -> new NotFoundException(404, "해당 포스트가 존재하지 않습니다."));

        Optional<Comment> findBySubComment = commentRepository.findByIdAndParentId(commentId, parentId);

        Comment subComment = findBySubComment.orElseThrow(() -> new NotFoundException(404, "해당 대댓글 존재하지 않습니다."));

        commentRepository.delete(subComment);
    }
}
