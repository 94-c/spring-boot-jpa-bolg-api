package com.blog.api.dto;

import lombok.Builder;
import lombok.Data;

import java.beans.ConstructorProperties;

@Builder
@Data
public class SearchDto {

    private String category;
    private String searchType;
    private String query;

    @ConstructorProperties({"category", "search_type", "query"})
    public SearchDto(String category, String searchType, String query) {
        this.category = category;
        this.searchType = searchType;
        this.query = query;
    }

}
