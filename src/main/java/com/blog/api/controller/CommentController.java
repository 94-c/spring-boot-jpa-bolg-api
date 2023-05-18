package com.blog.api.controller;

import com.blog.api.dto.CommentDto;
import com.blog.api.response.SuccessResponse;
import com.blog.api.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/posts/{id}/commnet")
    @ResponseStatus(value = HttpStatus.CREATED)
    public SuccessResponse<CommentDto> createComment(@PathVariable(name = "id") Long postId,
                                                 @Valid @RequestBody CommentDto commentDto,
                                                 Principal principal) {
        CommentDto commnet = commentService.createComment(postId, commentDto, principal.getName());

        return SuccessResponse.success(commnet);
    }
}
