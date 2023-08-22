package com.supercoding.commerce03.service.product;

import com.supercoding.commerce03.repository.product.entity.Product;
import com.supercoding.commerce03.repository.wish.WishRepository;
import com.supercoding.commerce03.repository.wish.entity.Wish;
import com.supercoding.commerce03.web.dto.product.ProductDto;
import lombok.RequiredArgsConstructor;;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SearchWishList {
    private final WishRepository wishRepository;

    public ProductDto setIsLikedOne(ProductDto product){
        List<Long> wishedList = getProductIdsFromWishList();
        if (wishedList.contains(product.getId())) {
            product.setLiked(true);
        }
        return product;
    }

    public List<ProductDto> setIsLiked(List<ProductDto> products){
        //List<Long> targetList = products.stream().map(e -> e.getId()).collect(Collectors.toList());
        List<Long> wishedList = getProductIdsFromWishList();

        for (ProductDto product : products) {
            if (wishedList.contains(product.getId())) {
                product.setLiked(true);
            }
        }
        return products;
    }



    public List<ProductDto> setIsLikedForDto(List<ProductDto> products){
        //List<Long> targetList = products.stream().map(e -> e.getId()).collect(Collectors.toList());
        List<Long> wishedList = getProductIdsFromWishList();

        for (ProductDto product : products) {
            if (wishedList.contains(product.getId())) {
                product.setLiked(true);
            }
        }
        return products;
    }

    public List<Long> getProductIdsFromWishList() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication.getPrincipal() == "anonymousUser")) {
            String userId = authentication.getName(); // 사용자의 ID (일반적으로 username)
            System.out.println("-------SearchWishList------" + userId);
            System.out.println("-------SearchWishList------" + authentication.getPrincipal());
            List<Wish> wishes = wishRepository.findByUserId(Long.parseLong(userId));
            return wishes.stream()
                    .map(wish -> wish.getProduct().getId())
                    .collect(Collectors.toList());
        } else {
            // 인증되지 않은 상태에서는 userId와 principal을 사용하지 않음
            System.out.println("-------SearchWishList------ Not authenticated");
            return Collections.emptyList();
        }
    }



}
