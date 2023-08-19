package com.supercoding.commerce03.service.product;

import com.supercoding.commerce03.repository.product.ProductRepository;
import com.supercoding.commerce03.repository.product.entity.Product;
import com.supercoding.commerce03.repository.user.UserRepository;
import com.supercoding.commerce03.repository.user.entity.User;
import com.supercoding.commerce03.repository.wish.WishRepository;
import com.supercoding.commerce03.repository.wish.entity.Wish;
import com.supercoding.commerce03.service.product.exception.ProductErrorCode;
import com.supercoding.commerce03.service.product.exception.ProductException;
import com.supercoding.commerce03.web.dto.product.GetRequestDto;
import com.supercoding.commerce03.web.dto.product.GetWishListDto;
import com.supercoding.commerce03.web.dto.product.ProductDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final EntityManager entityManager;
    private final ProductRepository productRepository;
    private final WishRepository wishRepository;
    private final UserRepository userRepository;

    @Transactional
    public List<ProductDto> getProductsList(GetRequestDto getRequestDto, String searchWord, int pageNumber) {
        int pageSize = 15;

        String query =
                "SELECT NEW com.supercoding.commerce03.web.dto.product.ProductDto(" +
                        "p.id, p.imageUrl, p.animalCategory, p.productCategory, p.productName, " +
                        "p.price, p.description, p.stock, p.wishCount, p.purchaseCount, p.createdAt) " +
                "FROM Product p " +
                "WHERE p.animalCategory = :animalCategory " +
                "AND p.productCategory = :productCategory ";

        if(searchWord != null && !searchWord.isEmpty()){
            query += "AND p.productName LIKE :searchWord ";
        }

        if ("wishCount".equals(getRequestDto.getSortBy())) {
            //인기순
            query += "ORDER BY p.wishCount DESC";
        } else if ("createdAt".equals(getRequestDto.getSortBy())) {
            //최신순
            query += "ORDER BY p.createdAt DESC";
        } else {
            // 기본 정렬 기준 (가격순)
            query += "ORDER BY p.price ASC";
        }

        TypedQuery<ProductDto> jpqlQuery = entityManager.createQuery(query, ProductDto.class);
        jpqlQuery.setParameter("animalCategory", getRequestDto.getAnimalCategory());
        jpqlQuery.setParameter("productCategory", getRequestDto.getProductCategory());
        if(searchWord != null && !searchWord.isEmpty()) {
            jpqlQuery.setParameter("searchWord", "%" + searchWord + "%");
        }

        jpqlQuery.setFirstResult((pageNumber - 1) * pageSize); // Offset 계산
        jpqlQuery.setMaxResults(pageSize); // Limit 설정

        return jpqlQuery.getResultList();
    }

    @Transactional
    public List<ProductDto> getPopularTen(GetRequestDto getRequestDto) {
//        String query =
//                "SELECT p FROM Product p " +
//                "WHERE p.animalCategory = :animalCategory " +
//                //"AND p.productCategory = :productCategory " +
//                "ORDER BY p.wishCount DESC";
        String query =
                "SELECT NEW com.supercoding.commerce03.web.dto.product.ProductDto(" +
                        "p.id, p.imageUrl, p.animalCategory, p.productCategory, p.productName, " +
                        "p.price, p.description, p.stock, p.wishCount, p.purchaseCount, p.createdAt" +
                        ") " +
                        "FROM Product p " +
                        "WHERE p.animalCategory = :animalCategory " +
                        "AND p.productCategory = :productCategory " +
                        "ORDER BY p.wishCount DESC";

        TypedQuery<ProductDto> jpqlQuery = entityManager.createQuery(query, ProductDto.class);
        jpqlQuery.setParameter("animalCategory", getRequestDto.getAnimalCategory());
        jpqlQuery.setParameter("productCategory", getRequestDto.getProductCategory());
        jpqlQuery.setMaxResults(10); // JPQL은 LIMIT 쿼리를 지원하지 않는다고 한다.

        return jpqlQuery.getResultList();
    }

    @Transactional
    public List<ProductDto> getRecommendThree(GetRequestDto getRequestDto) {
//        String query =
//                "SELECT p " +
//                        "FROM Product p " +
//                        "WHERE p.animalCategory = :animalCategory " +
//                        //"AND p.productCategory = :productCategory " +
//                        "ORDER BY p.stock DESC";

        String query =
                "SELECT NEW com.supercoding.commerce03.web.dto.product.ProductDto(" +
                        "p.id, p.imageUrl, p.animalCategory, p.productCategory, p.productName, " +
                        "p.price, p.description, p.stock, p.wishCount, p.purchaseCount, p.createdAt" +
                        ") " +
                        "FROM Product p " +
                        "WHERE p.animalCategory = :animalCategory " +
                        //"AND p.productCategory = :productCategory " +
                        "ORDER BY p.stock DESC";

        TypedQuery<ProductDto> jpqlQuery = entityManager.createQuery(query, ProductDto.class);
        jpqlQuery.setParameter("animalCategory", getRequestDto.getAnimalCategory());
        //jpqlQuery.setParameter("productCategory", getRequestDto.getProductCategory());
        jpqlQuery.setMaxResults(3); // JPQL은 LIMIT 쿼리를 지원하지 않는다고 한다.

        return jpqlQuery.getResultList();
    }

    @Transactional
    public List<ProductDto> getMostPurchasedTree(GetRequestDto getRequestDto) {
//        String query =
//                "SELECT p FROM Product p " +
//                        "WHERE p.animalCategory = :animalCategory " +
//                        "ORDER BY p.purchaseCount DESC";

        String query =
                "SELECT NEW com.supercoding.commerce03.web.dto.product.ProductDto(" +
                        "p.id, p.imageUrl, p.animalCategory, p.productCategory, p.productName, " +
                        "p.price, p.description, p.stock, p.wishCount, p.purchaseCount, p.createdAt" +
                        ") " +
                        "FROM Product p " +
                        "WHERE p.animalCategory = :animalCategory " +
                        //"AND p.productCategory = :productCategory " +
                        "ORDER BY p.purchaseCount DESC";

        TypedQuery<ProductDto> jpqlQuery = entityManager.createQuery(query, ProductDto.class);
        jpqlQuery.setParameter("animalCategory", getRequestDto.getAnimalCategory());
        jpqlQuery.setMaxResults(3); // JPQL은 LIMIT 쿼리를 지원하지 않는다고 한다.
        return jpqlQuery.getResultList();
    }

    @Transactional
    public List<ProductDto> getProduct(Integer productId) {

        try {
            String query =
                    "SELECT NEW com.supercoding.commerce03.web.dto.product.ProductDto(" +
                            "p.id, p.imageUrl, p.animalCategory, p.productCategory, p.productName, " +
                            "p.price, p.description, p.stock, p.wishCount, p.purchaseCount, p.createdAt" +
                            ") " +
                            "FROM Product p WHERE p.id = :productId";
            TypedQuery<ProductDto> jpqlQuery = entityManager.createQuery(query, ProductDto.class);
            jpqlQuery.setParameter("productId", (long)productId);
            return jpqlQuery.getResultList();
        } catch (NoResultException e) {
            //status 400
            throw new ProductException(ProductErrorCode.THIS_PRODUCT_DOES_NOT_EXIST);
        }
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


