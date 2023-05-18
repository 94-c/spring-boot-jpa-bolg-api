package com.blog.api.repository.query;

import com.blog.api.dto.SearchDto;
import com.blog.api.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostQueryRepository {

    List<Post> postListQueryDSL(SearchDto searchDto);

    Page<Post> postListPagingQueryDSL(SearchDto searchDto, Pageable pageable);

}
