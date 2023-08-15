package com.supercoding.commerce03.web.controller.product;

import com.supercoding.commerce03.repository.product.entity.Product;
import com.supercoding.commerce03.service.product.ProductService;
import com.supercoding.commerce03.web.dto.product.DummyRequestDto;
import com.supercoding.commerce03.web.dto.product.DummyStoreDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class ProductController {

    private final ProductService productService;

    @GetMapping("/{animalCategory}/{productCategory}/{sortBy}")
    public List<Product> getProducts(
            @PathVariable Integer animalCategory,
            @PathVariable Integer productCategory,
            @PathVariable Integer sortBy,
            @RequestParam(required = false) String searchWord
    ) {
        // Call the ProductService to fetch products based on the provided parameters
        List<Product> products = productService.getProductsList(animalCategory, productCategory, sortBy, searchWord);
        return products;
    }

    @GetMapping("/{productId}")
    public Product getProduct(
            @PathVariable Integer productId

    ) {
        Product product = productService.getProduct();
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
