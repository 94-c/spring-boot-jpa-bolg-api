package com.blog.api.service;

import com.blog.api.dto.LikeDto;
import com.blog.api.entity.Like;
import com.blog.api.entity.Post;
import com.blog.api.entity.User;
import com.blog.api.entity.common.LocalDate;
import com.blog.api.exception.NotFoundException;
import com.blog.api.repository.LikeRepository;
import com.blog.api.repository.PostRepository;
import com.blog.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private static final String SUCCESS_LIKE_BOARD = "좋아요 처리 완료";
    private static final String SUCCESS_UNLIKE_BOARD = "좋아요 취소 완료";
    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;

    private User getUserInfo(String email) {
        Optional<User> byEmail = userRepository.findByEmail(email);

        return byEmail.orElseThrow(() -> new NotFoundException(404, "유저가 없습니다."));
    }

    public boolean hasLikePost(final Post post, final Long userId) {
        return likeRepository.findByPostAndUserId(post, userId)
                .isPresent();
    }

    public LikeDto createLikePosts(final Post post, final Long userId) {
        Like likePost = Like.builder()
                .post(post)
                .userId(userId)
                .date(LocalDate.builder()
                        .createdAt(LocalDateTime.now())
                        .build())
                .build();

        Like createLike = likeRepository.save(likePost);

        return LikeDto.convertToLikeDto(createLike);
    }

    private void removeLikePosts(final Post post, final Long userId) {
        Like likePost = likeRepository.findByPostAndUserId(post, userId)
                .orElseThrow(() -> new NotFoundException(404, "게시물을 찾을 수 없습니다."));

        likeRepository.delete(likePost);

    }

    @Transactional
    public LikeDto updateLikeOfPost(final Long postId, final String email) {
        User findUser = getUserInfo(email);

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException(404, "게시물을 찾을 수 없습니다."));

        if (!hasLikePost(post, findUser.getId())) {
            post.increaseLikeCount();
            return createLikePosts(post, findUser.getId());
        }

        post.decreaseLikeCount();
        removeLikePosts(post, findUser.getId());
        return null;
    }

}
