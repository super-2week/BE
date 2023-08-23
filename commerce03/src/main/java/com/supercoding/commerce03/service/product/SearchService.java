package com.supercoding.commerce03.service.product;

import com.supercoding.commerce03.repository.product.ProductRepository;
import com.supercoding.commerce03.web.dto.product.GetRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional
public class SearchService {
    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public List<String> searchFullText(GetRequestDto getRequestDto, String searchWord){

        String keyWord = searchWord + "*";

        List<Object[]> wordList = productRepository.fullTextSearch(keyWord);
        List<String> words = new ArrayList<>();
        wordList.forEach(w -> words.add((String)w[0]));
        return words;//.stream().map(SearchWordListDto::fromSearch).collect(Collectors.toList());

    }

}

