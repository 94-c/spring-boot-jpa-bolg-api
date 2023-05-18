package com.blog.api.repository.query;

import com.blog.api.dto.SearchDto;
import com.blog.api.entity.Category;
import com.blog.api.entity.Post;
import com.blog.api.util.enumerate.OrderType;
import com.blog.api.util.enumerate.SearchType;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

import static com.blog.api.entity.QCategory.category;
import static com.blog.api.entity.QPost.post;
import static com.blog.api.entity.QUser.user;

@RequiredArgsConstructor
public class PostQueryRepositoryImpl implements PostQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final Map<String, OrderType> orderTypeMap;
    private final Map<String, SearchType> searchTypeMap;

    @Override
    public List<Post> postListQueryDSL(SearchDto searchDto) {
        BooleanExpression postCategoryQuery = postCategoryQuery(searchDto.getCategory());

        List<Post> posts = jpaQueryFactory
                .selectFrom(post)
                .where(postCategoryQuery)
                .join(post.user, user).fetchJoin()
                .join(post.category, category).fetchJoin()
                .fetch();

        return posts;
    }

    @Override
    public Page<Post> postListPagingQueryDSL(SearchDto searchDto, Pageable pageable) {
        return null;
    }

    private BooleanExpression postCategoryQuery(String category) {
        if (StringUtils.hasLength(category)) {
            return post.category.eq(getPostCategoryQuery(category));
        }

        return null;
    }
    private Category getPostCategoryQuery(String name) {
        return jpaQueryFactory
                .selectFrom(category)
                .where(category.name.eq(name))
                .fetchOne();
    }
}
