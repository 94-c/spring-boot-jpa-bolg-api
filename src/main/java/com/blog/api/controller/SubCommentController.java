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

    @PostMapping("posts/{id}/comments")
    @ResponseStatus(value = HttpStatus.CREATED)
    public SuccessResponse<CommentDto> createSubComment(@PathVariable(name = "id") Long postId,
                                                     @Valid @RequestBody CommentDto commentDto,
                                                     Principal principal) {
        CommentDto comment = commentService.createComment(postId, commentDto, principal.getName());

        return SuccessResponse.success(comment);
    }

    @GetMapping("posts/{id}/comments/{commentId}")
    @ResponseStatus(value = HttpStatus.OK)
    public SuccessResponse<CommentDto> getSubComment(@PathVariable(name = "id") Long postId,
                                                  @PathVariable(name = "commentId") Long commentId) {
        CommentDto comment = commentService.getComment(postId, commentId);

        return SuccessResponse.success(comment);
    }

    @PutMapping("posts/{id}/comments/{commentId}")
    @ResponseStatus(value = HttpStatus.OK)
    public SuccessResponse<CommentDto> updateSubComment(@Valid @RequestBody CommentDto dto,
                                                     @PathVariable(name = "id") Long postId,
                                                     @PathVariable(name = "commentId") Long commentId) {
        CommentDto updateComment = commentService.updateComment(postId, commentId, dto);

        return SuccessResponse.success(updateComment);
    }

    @DeleteMapping("posts/{id}/comments/{commentId}")
    @ResponseStatus(value = HttpStatus.OK)
    public SuccessResponse<String> deleteSubComment(@PathVariable(name = "id") Long postId,
                                                 @PathVariable(name = "commentId") Long commentId) {
        commentService.deleteComment(postId, commentId);

        return SuccessResponse.success(null);
    }
}
