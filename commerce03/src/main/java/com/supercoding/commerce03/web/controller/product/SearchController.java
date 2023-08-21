package com.supercoding.commerce03.web.controller.product;

import com.supercoding.commerce03.service.product.SearchService;
import com.supercoding.commerce03.web.dto.product.GetRequestDto;
import com.supercoding.commerce03.web.dto.product.ProductDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class SearchController {

    private final SearchService searchService;

    @GetMapping(value={"v1/api/search"})
    public ResponseEntity<List<ProductDto>> searchText() {

        //메인페이지 상품리스트
        List<ProductDto> product = searchService.searchFullText();
        return ResponseEntity.ok(product);
    }

}
