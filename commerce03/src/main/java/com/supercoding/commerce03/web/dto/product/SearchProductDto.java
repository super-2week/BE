package com.supercoding.commerce03.web.dto.product;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SearchProductDto {
    private Long id;
    private String description;
    private String productName;
}
