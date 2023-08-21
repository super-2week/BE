package com.supercoding.commerce03.service.product;

import com.supercoding.commerce03.repository.product.ProductRepository;
import com.supercoding.commerce03.repository.product.entity.Product;
import com.supercoding.commerce03.web.dto.product.GetRequestDto;
import com.supercoding.commerce03.web.dto.product.ProductDto;
import com.supercoding.commerce03.web.dto.product.ProductResponseDto;
import com.supercoding.commerce03.web.dto.product.SearchWordListDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class SearchService {
    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public List<Object[]> searchFullText(GetRequestDto getRequestDto, String searchWord){

        String keyWord = searchWord;
        System.out.println(keyWord);

        List<Object[]> wordList = productRepository.fullTextSearch(keyWord);
        return wordList;//.stream().map(SearchWordListDto::fromSearch).collect(Collectors.toList());

    }

}

