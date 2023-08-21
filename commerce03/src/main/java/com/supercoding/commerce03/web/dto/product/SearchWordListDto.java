package com.supercoding.commerce03.web.dto.product;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SearchWordListDto {
    private String productName;

//    public static SearchWordListDto fromSearch(Object searchWord){
//        return SearchWordListDto.builder()
//                .productName(searchWord)
//                .build();
//    }
}
