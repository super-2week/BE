package com.supercoding.commerce03.service.product;

import com.supercoding.commerce03.repository.product.ProductRepository;
import com.supercoding.commerce03.repository.product.entity.Product;
import com.supercoding.commerce03.repository.user.UserRepository;
import com.supercoding.commerce03.repository.user.entity.User;
import com.supercoding.commerce03.repository.wish.WishRepository;
import com.supercoding.commerce03.repository.wish.entity.Wish;
import com.supercoding.commerce03.service.product.exception.ProductErrorCode;
import com.supercoding.commerce03.service.product.exception.ProductException;
import com.supercoding.commerce03.web.dto.product.*;
import com.supercoding.commerce03.web.dto.product.util.ConvertCategory;
import com.supercoding.commerce03.web.dto.product.util.ProductCategory;
import com.supercoding.commerce03.web.dto.product.util.SmallCategory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.*;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final WishRepository wishRepository;
    private final UserRepository userRepository;
    private final ConvertCategory convertCategory;
    private final SearchWishList searchWishList;


    public ProductService(ProductRepository productRepository, WishRepository wishRepository, UserRepository userRepository, ConvertCategory convertCategory, SearchWishList searchWishList) {
        this.productRepository = productRepository;
        this.wishRepository = wishRepository;
        this.userRepository = userRepository;
        this.convertCategory = convertCategory;
        this.searchWishList = searchWishList;
    }

    @Transactional(readOnly = true)
    public String getProductList(String searchWord, int pageNumber) {
        int pageSize;
        int totalLength = 0;
        JSONObject resultObject = new JSONObject();
        JSONArray resultArray = new JSONArray();
        List resultList = null;
        String[] searchWords = (searchWord != null) ? searchWord.split("\\+") : new String[0];
        Optional<String> firstSearchWord = Optional.ofNullable(searchWords.length >= 1 ? searchWords[0] : null);
        Optional<String> secondSearchWord = Optional.ofNullable(searchWords.length > 1 ? searchWords[1] : null);

        //첫페이지 32개, 다음 페이지 12개
        if(pageNumber == 1){
            pageSize = 32;
        }else {
            pageSize = 12;
        }

        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize); // pageNumber는 1부터 시작
        List<Object[]> rawResult = productRepository.getProductList(
                firstSearchWord.map(word -> "%" + word + "%").orElse(null),
                secondSearchWord.map(word -> "%" + word + "%").orElse(null),
                pageable
        );
        List<ProductDto> products = rawResult.stream()
                .map(productData -> {
                    BigInteger bigIntegerId = (BigInteger) productData[0];
                    Long id = bigIntegerId.longValue();
                    String imageUrl = (String) productData[1];
                    Integer animalCategory = (Integer) productData[2];
                    Integer productCategory = (Integer) productData[3];
                    String productName = (String) productData[4];
                    String modelNum = (String) productData[5];
                    String originLabel = (String) productData[6];
                    Integer price = (Integer) productData[7];
                    String description = (String) productData[8];
                    Integer stock = (Integer) productData[9];
                    Integer wishCount = (Integer) productData[10];
                    Integer purchaseCount = (Integer) productData[11];
                    Timestamp timestamp = (Timestamp) productData[12];
                    LocalDateTime createdAt = timestamp.toLocalDateTime();
                    String storeName = (String) productData[13];

                    return ProductDto.builder()
                            .id(id)
                            .imageUrl(imageUrl)
                            .animalCategory(animalCategory)
                            .productCategory(productCategory)
                            .productName(productName)
                            .modelNum(modelNum)
                            .originLabel(originLabel)
                            .price(price)
                            .description(description)
                            .stock(stock)
                            .wishCount(wishCount)
                            .purchaseCount(purchaseCount)
                            .createdAt(createdAt)
                            .storeName(storeName)
                            .build();
                })
                .collect(Collectors.toList());

        List<ProductDto> checkedProducts = searchWishList.setIsLiked(products);
        resultList = checkedProducts.stream().map(ProductResponseDto::fromEntity).collect(Collectors.toList());
//        if ("wishCount".equals(sortBy)) {
//            //인기순
//            resultList = checkedProducts.stream().map(ProductResponseDto::fromEntity).sorted(Comparator.comparingInt(ProductResponseDto::getWishCount).reversed()).collect(Collectors.toList());
//
//        } else if ("createdAt".equals(sortBy)) {
//            //최신순
//            resultList = checkedProducts.stream().map(ProductResponseDto::fromEntity).sorted(Comparator.comparing(ProductResponseDto::getCreatedAt).reversed()).collect(Collectors.toList());
//        } else {
//            // 기본 정렬 기준 (가격순)
//            resultList = checkedProducts.stream().map(ProductResponseDto::fromEntity).sorted(Comparator.comparingInt(ProductResponseDto::getPrice)).collect(Collectors.toList());
//        }

        resultObject.put("products", resultList);
        if(pageNumber==1) {
            //첫페이지 에서만 상품의 totalLength(총 개수) 반환
            totalLength = productRepository.getCount(
                    firstSearchWord.map(word -> "%" + word + "%").orElse(null),
                    secondSearchWord.map(word -> "%" + word + "%").orElse(null));
            resultObject.put("totalLength", totalLength);
        }
        return resultArray.put(resultObject).toString();
    }


    @Transactional(readOnly = true)
    public String getProductsListWithFilter(GetRequestDto getRequestDto, String searchWord, int pageNumber) {
        int pageSize;
        int totalLength = 0;
        Integer animalCategory = convertCategory.convertAnimalCategory(getRequestDto.getAnimalCategory());
        Integer productCategory = convertCategory.convertProductCategory(getRequestDto.getAnimalCategory(), getRequestDto.getProductCategory());
        String sortBy = convertCategory.convertSortBy(getRequestDto.getSortBy());
        JSONObject resultObject = new JSONObject();
        JSONArray resultArray = new JSONArray();
        List resultList = null;
        String[] searchWords = (searchWord != null) ? searchWord.split("\\+") : new String[0];
        Optional<String> firstSearchWord = Optional.ofNullable(searchWords.length >= 1 ? searchWords[0] : null);
        Optional<String> secondSearchWord = Optional.ofNullable(searchWords.length > 1 ? searchWords[1] : null);

        if(pageNumber == 1){
            pageSize = 32;
        }else {
            pageSize = 12;
        }

        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize); // pageNumber는 1부터 시작
        List<ProductDto> products = productRepository.getProductsWithFilters(
                animalCategory,
                productCategory,
                firstSearchWord.map(word -> "%" + word + "%").orElse(null),
                secondSearchWord.map(word -> "%" + word + "%").orElse(null),
                sortBy,
                pageable
        );

        List<ProductDto> checkedProducts = searchWishList.setIsLiked(products);

//        if(pageNumber == 1){
//            totalLength = products.size(); //32 혹은 그 미만
//            firstPageSize = products.size();
//        } else {
//            totalLength = firstPageSize + (pageNumber - 2) * 12 + products.size();
//        }

        if ("wishCount".equals(sortBy)) {
            //인기순
            resultList = checkedProducts.stream().map(ProductResponseDto::fromEntity).sorted(Comparator.comparingInt(ProductResponseDto::getWishCount).reversed()).collect(Collectors.toList());

        } else if ("createdAt".equals(sortBy)) {
            //최신순
            resultList = checkedProducts.stream().map(ProductResponseDto::fromEntity).sorted(Comparator.comparing(ProductResponseDto::getCreatedAt).reversed()).collect(Collectors.toList());
        } else {
            // 기본 정렬 기준 (가격순)
            resultList = checkedProducts.stream().map(ProductResponseDto::fromEntity).sorted(Comparator.comparing(ProductResponseDto::getPrice)).collect(Collectors.toList());
        }
        resultObject.put("products", resultList);
        if(pageNumber==1) {
            //첫페이지 에서만 상품의 totalLength(총 개수) 반환
            totalLength = productRepository.getCountWithFilter(
                    animalCategory,
                    productCategory,
                    firstSearchWord.map(word -> "%" + word + "%").orElse(null),
                    secondSearchWord.map(word -> "%" + word + "%").orElse(null));
            resultObject.put("totalLength", totalLength);
        }
        return resultArray.put(resultObject).toString();
    }

    @Transactional(readOnly=true)
    public List<ProductResponseDto> getPopularTen(GetRequestDto getRequestDto) {
        Integer animalCategory = convertCategory.convertAnimalCategory(getRequestDto.getAnimalCategory());
        Integer productCategory = convertCategory.convertProductCategory(getRequestDto.getAnimalCategory(), getRequestDto.getProductCategory());

        List<Product> products = productRepository.findTop10ByAnimalCategoryAndProductCategoryOrderByWishCountDesc(animalCategory, productCategory);
        List<ProductDto> productList = products.stream().map(ProductDto::fromEntity).collect(Collectors.toList());
        List<ProductDto> checkedProducts = searchWishList.setIsLiked(productList);
        return checkedProducts.stream().map(ProductResponseDto::fromEntity).sorted(Comparator.comparingInt(ProductResponseDto::getWishCount).reversed()).collect(Collectors.toList());
    }

    @Transactional(readOnly=true)
    public String getRecommendThree(GetRequestDto getRequestDto) {
        Integer animalCategory = convertCategory.convertAnimalCategory(getRequestDto.getAnimalCategory());
        Integer[] productCategories = convertCategory.assignProductCategoryList(getRequestDto.getAnimalCategory());
        JSONArray jsonArray = new JSONArray();

        for (Integer productCategory : productCategories) {
        List<Product> products = productRepository.findTop3ByAnimalCategoryAndProductCategoryOrderByStockDesc(animalCategory, productCategory);
        List<ProductDto> productList = products.stream().map(ProductDto::fromEntity).collect(Collectors.toList());
        List<ProductDto> checkedProducts = searchWishList.setIsLiked(productList);
        List<ProductResponseDto> resultList = checkedProducts.stream().map(ProductResponseDto::fromEntity).collect(Collectors.toList());

        JSONObject resultObject = new JSONObject();
        if(animalCategory == 3) {
            resultObject.put("product", resultList);
            resultObject.put("category", SmallCategory.getByCode(productCategory));
        } else{
            resultObject.put("product", resultList);
            resultObject.put("category", ProductCategory.getByCode(productCategory));
        }
        jsonArray.put(resultObject);
        }
        return jsonArray.toString();
    }

    @Transactional(readOnly=true)
    public String getMostPurchased(GetRequestDto getRequestDto) {
        Integer animalCategory = convertCategory.convertAnimalCategory(getRequestDto.getAnimalCategory());
        Integer[] productCategories = convertCategory.assignProductCategoryList(getRequestDto.getAnimalCategory());
        JSONArray jsonArray = new JSONArray();

        for (Integer productCategory : productCategories) {
            List<Product> products = productRepository.findTop4ByAnimalCategoryAndProductCategoryOrderByPurchaseCountDesc(animalCategory, productCategory);
            List<ProductDto> productList = products.stream().map(ProductDto::fromEntity).collect(Collectors.toList());
            List<ProductDto> checkedProducts = searchWishList.setIsLiked(productList);
            List<ProductResponseDto> resultList = checkedProducts.stream().map(ProductResponseDto::fromEntity).collect(Collectors.toList());

            JSONObject resultObject = new JSONObject();
            if(animalCategory == 3) {
                resultObject.put("product", resultList);
                resultObject.put("category", SmallCategory.getByCode(productCategory));
            } else{
                resultObject.put("product", resultList);
                resultObject.put("category", ProductCategory.getByCode(productCategory));
            }
            jsonArray.put(resultObject);
        }
        return jsonArray.toString();
    }

    @Transactional(readOnly = true)
    public ProductResponseDto getProduct(Long productId) {
            Product product = productRepository.findProductById(productId)
                    .orElseThrow(() -> new ProductException(ProductErrorCode.THIS_PRODUCT_DOES_NOT_EXIST));

            ProductDto checkedProduct = searchWishList.setIsLikedOne(ProductDto.fromEntity(product));
            return ProductResponseDto.fromEntity(checkedProduct);
    }

    @Transactional
    public Wish addWishList(long userId, long productId) {
        User validatedUser = validateUser(userId);
        Product validatedProduct = validateProduct(productId);

        if (existsInWishList(userId, productId)) {
            throw new ProductException(ProductErrorCode.ALREADY_EXISTS_IN_WISHLIST);
        }

        return wishRepository.save(Wish.builder()
                                .user(validatedUser)
                                .product(validatedProduct)
                                .build());
    }

    @Transactional
    public List<GetWishListDto> getWishList(long userId) {
        User validatedUser = validateUser(userId);
        List<Wish> wishList = wishRepository.findByUserId(validatedUser.getId()); //없으면 빈 배열을 반환해야 한다.
        return wishList.stream()
                .map(wish -> new GetWishListDto(wish.getId(), wish.getProduct()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteWishList(long userId, long productId) {
        Wish targetWish = wishRepository.findByUserIdAndProductId(userId, productId)
                .orElseThrow(()->new ProductException(ProductErrorCode.NOT_FOUND_IN_WISHLIST)); //없으면 예외처리

        wishRepository.delete(targetWish);
    }

    private User validateUser(long userId){
        return userRepository.findById(userId)
                .orElseThrow(() -> new ProductException(ProductErrorCode.USER_NOT_FOUND));
    }

    private Product validateProduct(long productId){
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductException(ProductErrorCode.THIS_PRODUCT_DOES_NOT_EXIST));

        return product;
    }

    private boolean existsInWishList(long userId, long productId){
        return wishRepository.existsByUserIdAndProductId(userId, productId);
    }

    public List<Map<String, Object>> getNaviData() {
        List<Map<String, Object>> naviData = new ArrayList<>();
        String[] animalIds = {"dog", "cat", "small"};
        String[] productLabels = {"food", "snack", "clean", "tableware", "house", "cloth"};
        String[] productValues = {"사료", "간식", "위생", "급식기/급수기", "집/울타리", "의류/악세사리"};
        String[] smallProductLabels = {"food", "equipment", "house"};
        String[] smallProductValues = {"사료", "기구", "집/울타리"};

        for (String animalId : animalIds) {
            List<Map<String, String>> productCategoryList = new ArrayList<>();

            if (animalId.equals("small")) {
                for (int i = 0; i < smallProductLabels.length; i++) {
                    Map<String, String> categoryMap = new HashMap<>();
                    categoryMap.put("label", smallProductLabels[i]);
                    categoryMap.put("value", smallProductValues[i]);
                    productCategoryList.add(categoryMap);
                }
            } else {
                for (int i = 0; i < productLabels.length; i++) {
                    Map<String, String> categoryMap = new HashMap<>();
                    categoryMap.put("label", productLabels[i]);
                    categoryMap.put("value", productValues[i]);
                    productCategoryList.add(categoryMap);
                }
            }

            Map<String, Object> naviDataMap = new HashMap<>();
            naviDataMap.put("id", animalId);
            naviDataMap.put("label", getAnimalLabel(animalId));
            naviDataMap.put("productCategory", productCategoryList);
            naviData.add(naviDataMap);
        }
        return naviData;

    }

    private String getAnimalLabel(String animalId) {
        if ("dog".equals(animalId)) {
            return "강아지";
        } else if ("cat".equals(animalId)) {
            return "고양이";
        } else if ("small".equals(animalId)) {
            return "소동물";
        }
        return "";
    }


}


