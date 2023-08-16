package com.supercoding.commerce03.web.controller.product;

import com.supercoding.commerce03.repository.product.entity.Product;
import com.supercoding.commerce03.service.product.ProductService;
import com.supercoding.commerce03.web.dto.product.DummyRequestDto;
import com.supercoding.commerce03.web.dto.product.DummyStoreDto;
import com.supercoding.commerce03.web.dto.product.GetRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
     * @return
     */
    @GetMapping(value={
            "/",
            "/{animalCategory}" ,
            "/{animalCategory}/{productCategory}",
            "/{animalCategory}/{productCategory}/{sortBy}"})
    public List<Product> getProducts(
            @PathVariable(required = false) String animalCategory,
            @PathVariable(required = false) String productCategory,
            @PathVariable(required = false) String sortBy,
            @RequestParam(required = false) String searchWord
    ) {
        GetRequestDto getRequestDto = new GetRequestDto(animalCategory, productCategory, sortBy);
        // Call the ProductService to fetch products based on the provided parameters
        List<Product> products = productService.getProductsList(getRequestDto, searchWord);
        return products;
    }

    /**
     * 싱픔 상세페이지
     * @param productId
     * @return
     */
    @GetMapping("/product/{productId}")
    public Product getProduct(
            @PathVariable Integer productId

    ) {
        Product product = productService.getProduct(productId);
        return product;
    }

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
