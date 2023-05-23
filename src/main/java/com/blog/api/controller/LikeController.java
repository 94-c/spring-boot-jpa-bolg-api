package com.blog.api.controller;

import com.blog.api.dto.LikeDto;
import com.blog.api.service.LikeService;
import com.blog.api.util.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@Slf4j
public class LikeController {

    private final LikeService likeService;

    @PostMapping("posts/{id}/likes")
    @ResponseStatus(HttpStatus.OK)
    public SuccessResponse<LikeDto> likePost(@PathVariable(name = "id") Long postId,
                                             Principal principal) {

        LikeDto like = likeService.updateLikeOfPost(postId, principal.getName());

        return SuccessResponse.success(like);
    }
}
