package com.supercoding.commerce03.web.controller.product;

import com.supercoding.commerce03.repository.product.entity.Product;
import com.supercoding.commerce03.repository.wish.entity.Wish;
import com.supercoding.commerce03.service.product.ProductService;
import com.supercoding.commerce03.web.dto.product.DummyRequestDto;
import com.supercoding.commerce03.web.dto.product.DummyStoreDto;
import com.supercoding.commerce03.web.dto.product.GetRequestDto;
import com.supercoding.commerce03.web.dto.product.GetResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ProductController {

    private final ProductService productService;

    /**
     * 메인페이지
     * @param animalCategory
     * @param productCategory
     * @param sortBy
     * @param searchWord
     * @param page
     * @return
     */
    @GetMapping(value={
            "/",
            "/{animalCategory}" ,
            "/{animalCategory}/{productCategory}",
            "/{animalCategory}/{productCategory}/{sortBy}"})
    public ResponseEntity<GetResponseDto> getProducts(
            @PathVariable(required = false) String animalCategory,
            @PathVariable(required = false) String productCategory,
            @PathVariable(required = false) String sortBy,
            @RequestParam(required = false) String searchWord,
            @RequestParam(required = false) Integer page
    ) {
        GetRequestDto getRequestDto = new GetRequestDto(animalCategory, productCategory, sortBy);
        log.info("animalCategory: " + animalCategory + getRequestDto.getAnimalCategory());
        log.info("productCategory: " + productCategory + getRequestDto.getProductCategory());
        log.info("sortBy: " + sortBy + getRequestDto.getSortBy());
        log.info("searchWord: " + searchWord);
        log.info("page: " + page);
        int pageNumber = (page != null) ? page : 1; // null이면 기본값 1

        //메인페이지 상품리스트
        List<Product> products = productService.getProductsList(getRequestDto, searchWord, pageNumber);

        //해당 카테고리 인기 Top10
        List<Product> popularList = productService.getPopularTen(getRequestDto);

        //해당 카테고리 추천 상품 3종
        List<Product> recommendList = productService.getRecommendThree(getRequestDto);

        GetResponseDto response = new GetResponseDto(products, popularList, recommendList);
        return ResponseEntity.ok(response);
    }

    /**
     * 싱픔 상세페이지
     * @param productId
     * @return
     */
    @GetMapping("/product/{productId}")
    public ResponseEntity<Product> getProduct(
            @PathVariable Integer productId

    ) {
        Product product = productService.getProduct(productId);
        return ResponseEntity.ok(product);
    }

    @GetMapping("/product/wish")
    public ResponseEntity<List<Wish>> getWishList(){
        //TODO: 로그인한 유저정보 가져오기
        //Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //String email = authentication.getName();
        int userId = 1;
        List<Wish> wishList = productService.getWishList(userId);
        return ResponseEntity.ok(wishList);
    }

    @PostMapping("/product/wish/{productId}")
    public ResponseEntity<Wish> addWishList(
            @PathVariable Integer productId
    ){
        //TODO: 로그인한 유저정보 가져오기
        //Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //String email = authentication.getName();
        int userId = 1;
        Wish wish = productService.addWishList((long)userId, (long)productId);
        return ResponseEntity.ok(wish);
    }

    @DeleteMapping("/product/wish/{productId}")


    @PostMapping("/dummy")
    @ResponseBody
    public String createDummy(@RequestBody DummyRequestDto dummyRequestDto){
        try {
            productService.handleDummyInsertion(dummyRequestDto);
            return String.valueOf(new ResponseEntity<>("Success", HttpStatus.OK));
        } catch (Exception e) {
            e.printStackTrace();
            return String.valueOf(new ResponseEntity<>("Failed", HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @PostMapping("/dummy/store")
    @ResponseBody
    public String createDummyStore(@RequestBody DummyStoreDto dummyStoreDto){
        System.out.println(dummyStoreDto);
        try {
            productService.handleDummyStore(dummyStoreDto);
            return String.valueOf(new ResponseEntity<>("Success", HttpStatus.OK));
        } catch (Exception e) {
            e.printStackTrace();
            return String.valueOf(new ResponseEntity<>("Failed", HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

}
