package com.blog.api.controller;

import com.blog.api.dto.CommentDto;
import com.blog.api.service.CommentService;
import com.blog.api.service.SubCommentService;
import com.blog.api.util.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class SubCommentController {

    private final SubCommentService subCommentService;

    @PostMapping("posts/{id}/comments/{commentId}")
    @ResponseStatus(value = HttpStatus.CREATED)
    public SuccessResponse<CommentDto> createSubComment(@PathVariable(name = "id") Long postId,
                                                        @PathVariable(name = "commentId") Long commentId,
                                                        @Valid @RequestBody CommentDto commentDto,
                                                        Principal principal) {
        CommentDto subComment = subCommentService.createSubComment(postId, commentId, commentDto, principal.getName());

        return SuccessResponse.success(subComment);
    }

    @GetMapping("posts/{id}/comments/{commentId}/subComment/{subCommentId}")
    @ResponseStatus(value = HttpStatus.OK)
    public SuccessResponse<CommentDto> getSubComment(@PathVariable(name = "id") Long postId,
                                                     @PathVariable(name = "commentId") Long commentId,
                                                     @PathVariable(name = "subCommentId") Long subCommentId) {

        CommentDto subComment = subCommentService.getSubComment(postId, commentId, subCommentId);

        return SuccessResponse.success(subComment);
    }

    @PutMapping("posts/{id}/comments/{commentId}/subComment/{subCommentId}")
    @ResponseStatus(value = HttpStatus.OK)
    public SuccessResponse<CommentDto> updateSubComment(@Valid @RequestBody CommentDto dto,
                                                        @PathVariable(name = "id") Long postId,
                                                        @PathVariable(name = "commentId") Long commentId,
                                                        @PathVariable(name = "subCommentId") Long subCommentId) {

        CommentDto updateSubComment = subCommentService.updateSubComment(postId, commentId, subCommentId, dto);

        return SuccessResponse.success(updateSubComment);
    }

    @DeleteMapping("posts/{id}/comments/{commentId}/subComment/{subCommentId}")
    @ResponseStatus(value = HttpStatus.OK)
    public SuccessResponse<String> deleteSubComment(@PathVariable(name = "id") Long postId,
                                                    @PathVariable(name = "commentId") Long commentId,
                                                    @PathVariable(name = "subCommentId") Long subCommentId) {
        subCommentService.deleteSubComment(postId, commentId, subCommentId);

        return SuccessResponse.success(null);
    }
}
