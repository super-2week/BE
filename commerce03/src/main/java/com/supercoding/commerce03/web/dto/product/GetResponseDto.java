package com.supercoding.commerce03.web.dto.product;

import com.supercoding.commerce03.repository.product.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class GetResponseDto {
    private List<Product> products;
    private List<Product> popularList;
    private List<Product> recommendList;

    public GetResponseDto(List<Product> products, List<Product> popularList, List<Product> recommendList) {
        this.products = products;
        this.popularList = popularList;
        this.recommendList = recommendList;
    }
}
