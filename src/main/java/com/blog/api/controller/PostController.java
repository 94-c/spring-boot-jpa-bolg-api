package com.blog.api.controller;

import com.blog.api.dto.PostDto;
import com.blog.api.dto.SearchDto;
import com.blog.api.response.SuccessResponse;
import com.blog.api.service.PostService;
import com.blog.api.util.PagingUtil;
import com.blog.api.util.resource.PostResource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PostController {

    private final PostService postService;

    @GetMapping("/posts")
    @ResponseStatus(value = HttpStatus.OK)
    public PostResource getAllPosts(
            @RequestParam(value = "pageNo", defaultValue = PagingUtil.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = PagingUtil.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = PagingUtil.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = PagingUtil.DEFAULT_SORT_DIREACTION, required = false) String sortDir) {

        return postService.findAllPosts(pageNo, pageSize, sortBy, sortDir);
    }

    @PostMapping("/posts")
    @ResponseStatus(value = HttpStatus.CREATED)
    public SuccessResponse<PostDto> createPost(@RequestBody PostDto postDTO, Principal principal) {
        log.info(principal.getName());

        PostDto post = postService.createPost(postDTO, principal.getName());

        return SuccessResponse.success(post);
    }

    @GetMapping("posts/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public SuccessResponse<PostDto> getPost(@PathVariable(name = "id") Long id) {
        PostDto post = postService.getPost(id);

        return SuccessResponse.success(post);
    }

    @PutMapping("posts/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public SuccessResponse<PostDto> updatePost(@RequestBody PostDto postDTO, @PathVariable(name = "id") Long postId) {
        PostDto postDTOResponse = postService.updatePost(postId, postDTO);

        return SuccessResponse.success(postDTOResponse);
    }

    @DeleteMapping("posts/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public SuccessResponse<String> deletePost(@PathVariable(name = "id") Long postId) {
        postService.deletePost(postId);

        return SuccessResponse.success(null);
    }

}
