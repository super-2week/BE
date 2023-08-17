package com.supercoding.commerce03.web.controller.product;

import com.supercoding.commerce03.repository.product.entity.Product;
import com.supercoding.commerce03.repository.wish.entity.Wish;
import com.supercoding.commerce03.service.product.ProductService;
import com.supercoding.commerce03.web.dto.product.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ProductController {

    private final ProductService productService;

    /**
     * 메인페이지
     * @return 카테고리 정보 응답
     */
    @GetMapping("/")
    public ResponseEntity<Map<String, Map<String, String>>> getIndex(){
        Map<String, Map<String, String>> response = productService.getIndex();
        return ResponseEntity.ok(response);
    }

    /**
     * 메인페이지 배너상품 리스트
     * @param animalCategory
     * @return
     */
    @GetMapping("/banner/{animalCategory}")
    public ResponseEntity<List<ProductDto>> getBanner(
            @PathVariable(required = false) String animalCategory
    ){
        GetRequestDto getRequestDto = new GetRequestDto(animalCategory);
        log.info("배너상품 동물분류: " + animalCategory + getRequestDto.getAnimalCategory());

        List<ProductDto> purchasedList = productService.getMostPurchasedTree(getRequestDto);
        return ResponseEntity.ok(purchasedList);
    }

    /**
     * 메인페이지 인기상품 TOP10
     * @param animalCategory
     * @param productCategory
     * @return
     */
    @GetMapping("/popular/{animalCategory}/{productCategory}")
    public ResponseEntity<List<ProductDto>> getPopular(
            @PathVariable(required = false) String animalCategory,
            @PathVariable(required = false) String productCategory
    ){
        GetRequestDto getRequestDto = new GetRequestDto(animalCategory, productCategory);
        log.info("인기상품 동물분류: " + animalCategory + getRequestDto.getAnimalCategory());
        log.info("인기상품 용품분류: " + animalCategory + getRequestDto.getProductCategory());

        //해당 카테고리 인기 Top10
        List<ProductDto> popularList = productService.getPopularTen(getRequestDto);
        return ResponseEntity.ok(popularList);
    }

    /**
     * 메인페이지 오늘의 추천상품
     * @param animalCategory
     * @return
     */
    @GetMapping("/recommend/{animalCategory}")
    public ResponseEntity<List<ProductDto>> getRecommends(
            @PathVariable(required = false) String animalCategory
    ){
        GetRequestDto getRequestDto = new GetRequestDto(animalCategory);
        log.info("추천상품 동물분류: " + animalCategory + getRequestDto.getAnimalCategory());
        //해당 카테고리 추천 상품 3종
        List<ProductDto> recommendList = productService.getRecommendThree(getRequestDto);
        return ResponseEntity.ok(recommendList);
    }

    /**
     * 상품 리스트 페이지
     * @param animalCategory
     * @param productCategory
     * @param sortBy
     * @param searchWord
     * @param page
     * @return
     */
    @GetMapping(value={
            "/product/{animalCategory}" ,
            "/product/{animalCategory}/{productCategory}",
            "/product/{animalCategory}/{productCategory}/{sortBy}"})
    public ResponseEntity<List<Product>> getProducts(
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

        return ResponseEntity.ok(products);
    }

    /**
     * 싱픔 상세페이지
     * @param productId
     * @return
     */
    @GetMapping("v1/api/product/{productId}")
    public ResponseEntity<List<Product>> getProduct(
            @PathVariable Integer productId

    ) {
        List<Product> product = productService.getProduct(productId);
        return ResponseEntity.ok(product);
    }

    /**
     * 유저의 관심상품 조회
     * @return
     */
    @GetMapping("/product/wish")
    public ResponseEntity<List<Wish>> getWishList(){
        //TODO: 로그인한 유저정보 가져오기
        //Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //String email = authentication.getName();
        long userId = 1L;
        List<Wish> wishList = productService.getWishList(userId);
        return ResponseEntity.ok(wishList);
    }

    /**
     * 관심상품 등록
     * @param productId
     * @return
     */
    @PostMapping("/product/wish/{productId}")
    public ResponseEntity<Wish> addWishList(
            @PathVariable Integer productId
    ){
        //TODO: 로그인한 유저정보 가져오기
        //Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //String email = authentication.getName();
        long userId = 1L;
        Wish wish = productService.addWishList(userId, (long)productId);
        return ResponseEntity.ok(wish);
    }

    /**
     * 관심상품 삭제
     * @param productId
     * @return
     */
    @DeleteMapping("/product/wish/{productId}")
    public ResponseEntity<ResponseMessageDto> deleteWishList(
            @PathVariable Integer productId
    ){
        //TODO: 로그인한 유저정보 가져오기
        //Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //String email = authentication.getName();
        long userId = 1L;
        productService.deleteWishList(userId, (long)productId);
        return ResponseEntity.ok(new ResponseMessageDto("관심상품 삭제됨"));
    }

}
