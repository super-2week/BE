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
import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class ProductService {

    private final EntityManager entityManager;
    private final ProductRepository productRepository;
    private final WishRepository wishRepository;
    private final UserRepository userRepository;
    private final ConvertCategory convertCategory;
    private final SearchWishList searchWishList;
    private int firstPageSize = 0;

    public ProductService(EntityManager entityManager, ProductRepository productRepository, WishRepository wishRepository, UserRepository userRepository, ConvertCategory convertCategory, SearchWishList searchWishList) {
        this.entityManager = entityManager;
        this.productRepository = productRepository;
        this.wishRepository = wishRepository;
        this.userRepository = userRepository;
        this.convertCategory = convertCategory;
        this.searchWishList = searchWishList;
    }


    @Transactional
    public String getProductsList(GetRequestDto getRequestDto, String searchWord, int pageNumber) {
        int pageSize;
        int totalLength = 0;
        Integer animalCategory = convertCategory.convertAnimalCategory(getRequestDto.getAnimalCategory());
        Integer productCategory = convertCategory.convertProductCategory(getRequestDto.getAnimalCategory(), getRequestDto.getProductCategory());
        String sortBy = convertCategory.convertSortBy(getRequestDto.getSortBy());
        JSONObject resultObject = new JSONObject();
        JSONArray resultArray = new JSONArray();
        List resultList = null;

//        String query =
//                "SELECT NEW com.supercoding.commerce03.web.dto.product.ProductDto(" +
//                        "p.id, p.imageUrl, p.animalCategory, p.productCategory, p.productName," +
//                        "p.modelNum, p.originLabel, p.price, p.description, p.stock, p.wishCount, p.purchaseCount, p.createdAt, p.store) " +
//                "FROM Product p " +
//                "WHERE p.animalCategory = :animalCategory " +
//                "AND p.productCategory = :productCategory ";
//
//        if(searchWord != null && !searchWord.isEmpty()){
//            query += "AND (p.productName LIKE :searchWord OR p.description LIKE :searchWord)";
//        }
//
//        if ("wishCount".equals(sortBy)) {
//            //인기순
//            query += "ORDER BY p.wishCount DESC";
//        } else if ("createdAt".equals(sortBy)) {
//            //최신순
//            query += "ORDER BY p.createdAt DESC";
//        } else {
//            // 기본 정렬 기준 (가격순)
//            query += "ORDER BY p.price ASC";
//        }

//        TypedQuery<ProductDto> jpqlQuery = entityManager.createQuery(query, ProductDto.class);
//        jpqlQuery.setParameter("animalCategory", animalCategory);
//        jpqlQuery.setParameter("productCategory", productCategory);
//        if(searchWord != null && !searchWord.isEmpty()) {
//            jpqlQuery.setParameter("searchWord", "%" + searchWord + "%");
//        }

        //첫페이지 32개, 다음 페이지 12개
        if(pageNumber == 1){
            pageSize = 32;
        }else {
            pageSize = 12;
        }

//        jpqlQuery.setFirstResult((pageNumber - 1) * pageSize); // Offset 계산
//        jpqlQuery.setMaxResults(pageSize); // Limit 설정
//        List<ProductDto> products = jpqlQuery.getResultList();

        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize); // pageNumber는 1부터 시작
        List<ProductDto> products = productRepository.getProductsWithFilters(
                animalCategory, productCategory, searchWord, sortBy, pageable
        );


        List<ProductDto> checkedProducts = searchWishList.setIsLiked(products);


        if(pageNumber == 1){
            totalLength = products.size(); //32 혹은 그 미만
            firstPageSize = products.size();
        } else {
            totalLength = firstPageSize + (pageNumber - 2) * 12 + products.size();
        }

        if ("wishCount".equals(sortBy)) {
            //인기순
            resultList = checkedProducts.stream().map(ProductResponseDto::fromEntity).sorted(Comparator.comparingInt(ProductResponseDto::getWishCount).reversed()).collect(Collectors.toList());

        } else if ("createdAt".equals(sortBy)) {
            //최신순
            resultList = checkedProducts.stream().map(ProductResponseDto::fromEntity).sorted(Comparator.comparing(ProductResponseDto::getCreatedAt).reversed()).collect(Collectors.toList());
        } else {
            // 기본 정렬 기준 (가격순)
            resultList = checkedProducts.stream().map(ProductResponseDto::fromEntity).sorted(Comparator.comparingInt(ProductResponseDto::getPrice)).collect(Collectors.toList());
        }
        resultObject.put("products", resultList);
        resultObject.put("totalLength", totalLength);
        return resultArray.put(resultObject).toString();
    }

    @Transactional
    public List<ProductResponseDto> getPopularTen(GetRequestDto getRequestDto) {
        Integer animalCategory = convertCategory.convertAnimalCategory(getRequestDto.getAnimalCategory());
        Integer productCategory = convertCategory.convertProductCategory(getRequestDto.getAnimalCategory(), getRequestDto.getProductCategory());
//        String query =
//                "SELECT NEW com.supercoding.commerce03.web.dto.product.ProductDto(" +
//                        "p.id, p.imageUrl, p.animalCategory, p.productCategory, p.productName, " +
//                        "p.modelNum, p.originLabel, p.price, p.description, p.stock, p.wishCount, p.purchaseCount, p.createdAt, p.store" +
//                        ") " +
//                        "FROM Product p " +
//                        "WHERE p.animalCategory = :animalCategory " +
//                        "AND p.productCategory = :productCategory " +
//                        "ORDER BY p.wishCount DESC";
//
//        TypedQuery<ProductDto> jpqlQuery = entityManager.createQuery(query, ProductDto.class);
//        jpqlQuery.setParameter("animalCategory", animalCategory);
//        jpqlQuery.setParameter("productCategory", productCategory);
//        jpqlQuery.setMaxResults(10); // JPQL은 LIMIT 쿼리를 지원하지 않는다고 한다.
//
//        List<ProductDto> products = jpqlQuery.getResultList();
        List<Product> products = productRepository.findTop10ByAnimalCategoryAndProductCategoryOrderByWishCountDesc(animalCategory, productCategory);
        List<ProductDto> productList = products.stream().map(ProductDto::fromEntity).collect(Collectors.toList());
        List<ProductDto> checkedProducts = searchWishList.setIsLiked(productList);
        return checkedProducts.stream().map(ProductResponseDto::fromEntity).sorted(Comparator.comparingInt(ProductResponseDto::getWishCount).reversed()).collect(Collectors.toList());
    }

    @Transactional
    public String getRecommendThree(GetRequestDto getRequestDto) {
        Integer animalCategory = convertCategory.convertAnimalCategory(getRequestDto.getAnimalCategory());
        Integer[] productCategories = convertCategory.assignProductCategoryList(getRequestDto.getAnimalCategory());
        JSONArray jsonArray = new JSONArray();

        for (Integer productCategory : productCategories) {
//        String query =
//                "SELECT NEW com.supercoding.commerce03.web.dto.product.ProductDto(" +
//                        "p.id, p.imageUrl, p.animalCategory, p.productCategory, p.productName, " +
//                        "p.modelNum, p.originLabel, p.price, p.description, p.stock, p.wishCount, p.purchaseCount, p.createdAt, p.store" +
//                        ") " +
//                        "FROM Product p " +
//                        "WHERE p.animalCategory = :animalCategory " +
//                        "AND p.productCategory = :productCategory " +
//                        "ORDER BY p.stock DESC";
//
//        TypedQuery<ProductDto> jpqlQuery = entityManager.createQuery(query, ProductDto.class);
//        jpqlQuery.setParameter("animalCategory", animalCategory);
//        jpqlQuery.setParameter("productCategory", productCategory);
//        jpqlQuery.setMaxResults(3); // JPQL은 LIMIT 쿼리를 지원하지 않는다고 한다.
//        List<ProductDto> products = jpqlQuery.getResultList();
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

    @Transactional
    public String getMostPurchased(GetRequestDto getRequestDto) {
        Integer animalCategory = convertCategory.convertAnimalCategory(getRequestDto.getAnimalCategory());
        Integer[] productCategories = convertCategory.assignProductCategoryList(getRequestDto.getAnimalCategory());
        JSONArray jsonArray = new JSONArray();

        for (Integer productCategory : productCategories) {
//            String query =
//                    "SELECT NEW com.supercoding.commerce03.web.dto.product.ProductDto(" +
//                            "p.id, p.imageUrl, p.animalCategory, p.productCategory, p.productName, " +
//                            "p.modelNum, p.originLabel, p.price, p.description, p.stock, p.wishCount, p.purchaseCount, p.createdAt, p.store" +
//                            ") " +
//                            "FROM Product p " +
//                            "WHERE p.animalCategory = :animalCategory " +
//                            "AND p.productCategory = :productCategory " +
//                            "ORDER BY p.purchaseCount DESC";
//
//            TypedQuery<ProductDto> jpqlQuery = entityManager.createQuery(query, ProductDto.class);
//            jpqlQuery.setParameter("animalCategory", animalCategory);
//            jpqlQuery.setParameter("productCategory", productCategory);
//            jpqlQuery.setMaxResults(4); // JPQL은 LIMIT 쿼리를 지원하지 않는다고 한다.
//            List<ProductDto> products = jpqlQuery.getResultList();

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

    @Transactional
    public ProductResponseDto getProduct(Long productId) {

//            String query =
//                    "SELECT NEW com.supercoding.commerce03.web.dto.product.ProductDto(" +
//                            "p.id, p.imageUrl, p.animalCategory, p.productCategory, p.productName, " +
//                            "p.modelNum, p.originLabel, p.price, p.description, p.stock, p.wishCount, p.purchaseCount, p.createdAt, p.store" +
//                            ") " +
//                            "FROM Product p WHERE p.id = :productId";
//            TypedQuery<ProductDto> jpqlQuery = entityManager.createQuery(query, ProductDto.class);
//            jpqlQuery.setParameter("productId", (long)productId);
//            List<ProductDto> products = jpqlQuery.getResultList();
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
        String[] productLabels = {"food", "snack", "clean", "tableware", "house", "cloth", "etc"};
        String[] productValues = {"사료", "간식", "위생", "급식기/급수기", "집/울타리", "의류/악세사리", "기타"};
        String[] smallProductLabels = {"food", "equipment", "house", "etc"};
        String[] smallProductValues = {"사료", "기구", "집/울타리", "기타"};

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


