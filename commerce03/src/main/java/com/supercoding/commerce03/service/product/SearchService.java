package com.supercoding.commerce03.service.product;

import com.supercoding.commerce03.repository.product.ProductRepository;
import com.supercoding.commerce03.web.dto.product.GetRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional
public class SearchService {
    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public List<String> searchFullText(GetRequestDto getRequestDto, String searchWord){

        String keyWord = "%" + searchWord + "%";

        List<String> words = productRepository.fullTextSearch(keyWord)
                .stream()
                .map(result -> (String) result[0])
                .collect(Collectors.toList());;

        return generateRelatedSearchTermsByAdjacency(words, searchWord);

    }

    /**
     * 빈도수에 따른 연관검색어
     * @param productNames
     * @param searchWord
     * @return
     */
    public List<String> generateRelatedTermsByFrequency(List<String> productNames, String searchWord) {
        // Tokenize and count word frequencies
        Map<String, Integer> wordFrequencies = new HashMap<>();
        for (String productName : productNames) {
            String[] words = productName.split("\\s+");
            for (String word : words) {
                //wordFrequencies.getOrDefault(word, 0) : 맵에서 해당 단어의 빈도수. 2개면 2 응답. 없으면 0응답.
                wordFrequencies.put(word, wordFrequencies.getOrDefault(word, 0) + 1);
            }
        }

        // Find related search terms based on co-occurring words
        List<String> relatedSearchTerms = new ArrayList<>();
        String[] searchWords = searchWord.split("\\s+");
        for (String word : searchWords) {
            if (wordFrequencies.containsKey(word)) {
                List<Map.Entry<String, Integer>> sortedWords = new ArrayList<>(wordFrequencies.entrySet());
                sortedWords.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue())); // 빈도수 내림차순 정렬

                System.out.println("Sorted Words:");
                for (Map.Entry<String, Integer> entry : sortedWords) {
                    System.out.println(entry.getKey() + ": " + entry.getValue());
                }


                for (Map.Entry<String, Integer> entry : sortedWords) {
                    relatedSearchTerms.add(entry.getKey());
                }
            }
        }

        // Remove duplicates and the original search word
        //relatedSearchTerms.remove(searchWord);
        //removeAll(Arrays.asList(searchWords));

        return relatedSearchTerms;
    }

    public List<String> generateRelatedSearchTermsByAdjacency(List<String> productNames, String searchWord) {
        List<String> adjacentWords = new ArrayList<>();
        //String[] searchWords = searchWord.split("\\s+");

        for (String productName : productNames){
            //상품명들 나누기
            String[] words = productName.split("\\s+");

            for (int i = 0; i < words.length; i++) {

                    if (words[i].equals(searchWord)) {  //예컨대 "강아지"가 겹치는 인덱스를 발견하면
                        //int start = Math.max(i - 1, 0);  //인접한 단어들의 범위를 결정한다. 앞으로 한 단어, 뒤로 2단어
                        int start = i; //앞으로는 안찾고, 뒤로만 두 단어
                        int end = Math.min(i + 2, words.length - 1);
                        StringBuilder adjacentWordBuilder = new StringBuilder();

                        for (int j = start; j <= end; j++) {
                            adjacentWordBuilder.append(words[j]).append(" ");
                        }
                        String adjacentWord = adjacentWordBuilder.toString();

                        if (!adjacentWord.equals(searchWord)) {
                            adjacentWords.add(adjacentWord);
                        }
                    }
            }
        }

        return adjacentWords;
    }







}

