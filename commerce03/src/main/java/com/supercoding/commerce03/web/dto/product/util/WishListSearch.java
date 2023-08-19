package com.supercoding.commerce03.web.dto.product.util;

import com.supercoding.commerce03.repository.wish.WishRepository;
import com.supercoding.commerce03.repository.wish.entity.Wish;
import com.supercoding.commerce03.service.security.Auth;
import com.supercoding.commerce03.service.security.AuthHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class WishListSearch {
    private final WishRepository wishRepository;

    public void getWishList(){
        Long userId = getUserIdFromJWT();
        List<Wish> wishList = wishRepository.findByUserId(userId);
        System.out.println("userId----------" + userId);
    }

    @Auth
    private Long getUserIdFromJWT(){
        return AuthHolder.getUserId();
    }

}
