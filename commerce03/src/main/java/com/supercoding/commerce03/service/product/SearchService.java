package com.supercoding.commerce03.service.product;

import com.supercoding.commerce03.repository.product.ProductRepository;
import com.supercoding.commerce03.repository.product.entity.Product;
import com.supercoding.commerce03.web.dto.product.GetRequestDto;
import com.supercoding.commerce03.web.dto.product.ProductDto;
import com.supercoding.commerce03.web.dto.product.ProductResponseDto;
import com.supercoding.commerce03.web.dto.product.SearchProductDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.sql.DataSource;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class SearchService {
    private final DataSource dataSource;
    private final ProductRepository productRepository;
    private final EntityManager entityManager;

    @Transactional(readOnly = true)
    public List<ProductResponseDto> searchFullText(GetRequestDto getRequestDto, String searchWord){

        System.out.println(searchWord);
        String keyWord = searchWord + '*';
        System.out.println(keyWord);
        List<Product> products = productRepository.fullTextSearch(keyWord);
        return products.stream().map(ProductResponseDto::fromSearch).collect(Collectors.toList());

    }

}

