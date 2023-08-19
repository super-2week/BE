package com.supercoding.commerce03.web.controller.product;

import com.supercoding.commerce03.repository.product.entity.Product;
import com.supercoding.commerce03.repository.wish.entity.Wish;
import com.supercoding.commerce03.service.product.ProductService;
import com.supercoding.commerce03.service.security.Auth;
import com.supercoding.commerce03.service.security.AuthHolder;
import com.supercoding.commerce03.web.dto.product.*;
import com.supercoding.commerce03.web.dto.product.util.WishListSearch;
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
    private final WishListSearch wishListSearch;
    /**
     * 메인페이지
     * @return 카테고리 정보 응답
     */
    @GetMapping("v1/api/navi")
    public ResponseEntity<List<Map<String, Object>>> getIndex(){
        List<Map<String, Object>> response = productService.getNaviData();
        return ResponseEntity.ok(response);
    }

    /**
     * 메인페이지 배너상품 리스트
     * @param animalCategory
     * @return
     */
    @Auth
    @CrossOrigin(origins = "*")
    @GetMapping("v1/api/banner/{animalCategory}")
    public ResponseEntity<String> getBanner(
            @PathVariable(required = false) String animalCategory
    ){
        //TODO: 로그인한 유저정보 가져오기
        Long userId = AuthHolder.getUserId();

        GetRequestDto getRequestDto = new GetRequestDto(animalCategory);
        log.info("배너상품 동물분류: " + animalCategory + getRequestDto.getAnimalCategory());

        String purchasedList = productService.getMostPurchased(getRequestDto, userId);
        return ResponseEntity.ok(purchasedList);
    }

    /**
     * 메인페이지 인기상품 TOP10
     * @param animalCategory
     * @param productCategory
     * @return
     */
    @CrossOrigin(origins = "*")
    @GetMapping("v1/api/popular/{animalCategory}/{productCategory}")
    public ResponseEntity<List<ProductDto>> getPopular(
            @PathVariable(required = false) String animalCategory,
            @PathVariable(required = false) String productCategory
    ){
        GetRequestDto getRequestDto = new GetRequestDto(animalCategory, productCategory);
        log.info("인기상품 동물분류: " + animalCategory + getRequestDto.getAnimalCategory());
        log.info("인기상품 용품분류: " + productCategory + getRequestDto.getProductCategory());

        //해당 카테고리 인기 Top10
        List<ProductDto> popularList = productService.getPopularTen(getRequestDto);
        return ResponseEntity.ok(popularList);
    }

    /**
     * 메인페이지 오늘의 추천상품
     * @param animalCategory
     * @return
     */
    @CrossOrigin(origins = "*")
    @GetMapping("v1/api/recommend/{animalCategory}")
    public ResponseEntity<String> getRecommends(
            @PathVariable(required = false) String animalCategory
    ){
        GetRequestDto getRequestDto = new GetRequestDto(animalCategory);
        log.info("추천상품 동물분류: " + animalCategory + getRequestDto.getAnimalCategory());
        //해당 카테고리 추천 상품 3종
        String recommendList = productService.getRecommendThree(getRequestDto);
        return ResponseEntity.ok(recommendList);
    }

    /**
     * 싱픔 상세페이지
     * @param productId
     * @return
     */
    @CrossOrigin(origins = "*")
    @GetMapping("v1/api/product/detail/{productId}")
    public ResponseEntity<List<ProductDto>> getProduct(
            @PathVariable Integer productId

    ) {
        List<ProductDto> product = productService.getProduct(productId);
        return ResponseEntity.ok(product);
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
    @CrossOrigin(origins = "*")
    @GetMapping(value={
            "v1/api/product/{animalCategory}" ,
            "v1/api/product/{animalCategory}/{productCategory}",
            "v1/api/product/{animalCategory}/{productCategory}/{sortBy}"})
    public ResponseEntity<List<ProductDto>> getProducts(
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
        List<ProductDto> products = productService.getProductsList(getRequestDto, searchWord, pageNumber);

        return ResponseEntity.ok(products);
    }

    /**
     * 유저의 관심상품 조회
     * @return
     */
    @CrossOrigin(origins = "*")
    @GetMapping("v1/api/product/wish")
    public ResponseEntity<List<GetWishListDto>> getWishList(){
        //TODO: 로그인한 유저정보 가져오기
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String email = authentication.getName();
        long userId = 1L;
        List<GetWishListDto> wishList = productService.getWishList(userId);
        return ResponseEntity.ok(wishList);
    }

    /**
     * 관심상품 등록
     * @param productId
     * @return
     */
    @PostMapping("v1/api/product/wish/{productId}")
    public ResponseEntity<ResponseMessageDto> addWishList(
            @PathVariable Integer productId
    ){
        //TODO: 로그인한 유저정보 가져오기
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String email = authentication.getName();

        long userId = 1L;
        Wish wish = productService.addWishList(userId, (long)productId);
        return ResponseEntity.ok(new ResponseMessageDto("상품명: " + wish.getProduct().getProductName() + "이(가) 관심상품으로 등록되었습니다."));
    }

    /**
     * 관심상품 삭제
     * @param productId
     * @return
     */
    @DeleteMapping("v1/api/product/wish/{productId}")
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
