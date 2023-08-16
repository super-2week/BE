package com.supercoding.commerce03.service.product;

import com.supercoding.commerce03.repository.product.ProductRepository;
import com.supercoding.commerce03.repository.product.entity.Product;
import com.supercoding.commerce03.repository.store.entity.Store;
import com.supercoding.commerce03.repository.user.UserRepository;
import com.supercoding.commerce03.repository.user.entity.User;
import com.supercoding.commerce03.repository.wish.WishRepository;
import com.supercoding.commerce03.repository.wish.entity.Wish;
import com.supercoding.commerce03.service.cart.exception.CartErrorCode;
import com.supercoding.commerce03.service.cart.exception.CartException;
import com.supercoding.commerce03.service.product.exception.ProductErrorCode;
import com.supercoding.commerce03.service.product.exception.ProductException;
import com.supercoding.commerce03.web.dto.product.DummyRequestDto;
import com.supercoding.commerce03.web.dto.product.DummyStoreDto;
import com.supercoding.commerce03.web.dto.product.GetRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class ProductService {

    @PersistenceContext
    private final EntityManager entityManager;
    private final ProductRepository productRepository;
    private final WishRepository wishRepository;
    private final UserRepository userRepository;

    public List<Product> getProductsList(GetRequestDto getRequestDto, String searchWord, int pageNumber) {
        int pageSize = 15;

        String query =
                "SELECT p FROM Product p " +
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

        Query jpqlQuery = entityManager.createQuery(query, Product.class);
        jpqlQuery.setParameter("animalCategory", getRequestDto.getAnimalCategory());
        jpqlQuery.setParameter("productCategory", getRequestDto.getProductCategory());
        if(searchWord != null && !searchWord.isEmpty()) {
            jpqlQuery.setParameter("searchWord", "%" + searchWord + "%");
        }

        jpqlQuery.setFirstResult((pageNumber - 1) * pageSize); // Offset 계산
        jpqlQuery.setMaxResults(pageSize); // Limit 설정

        return jpqlQuery.getResultList();
    }

    public List<Product> getPopularTen(GetRequestDto getRequestDto) {
        String query =
                "SELECT p FROM Product p " +
                "WHERE p.animalCategory = :animalCategory " +
                "AND p.productCategory = :productCategory " +
                "ORDER BY p.wishCount DESC";

        Query jpqlQuery = entityManager.createQuery(query, Product.class);
        jpqlQuery.setParameter("animalCategory", getRequestDto.getAnimalCategory());
        jpqlQuery.setParameter("productCategory", getRequestDto.getProductCategory());
        jpqlQuery.setMaxResults(10); // JPQL은 LIMIT 쿼리를 지원하지 않는다고 한다.

        return jpqlQuery.getResultList();
    }

    public List<Product> getRecommendThree(GetRequestDto getRequestDto) {
        String query =
                "SELECT p FROM Product p " +
                        "WHERE p.animalCategory = :animalCategory " +
                        "AND p.productCategory = :productCategory " +
                        "ORDER BY p.stock DESC";

        Query jpqlQuery = entityManager.createQuery(query, Product.class);
        jpqlQuery.setParameter("animalCategory", getRequestDto.getAnimalCategory());
        jpqlQuery.setParameter("productCategory", getRequestDto.getProductCategory());
        jpqlQuery.setMaxResults(3); // JPQL은 LIMIT 쿼리를 지원하지 않는다고 한다.

        return jpqlQuery.getResultList();
    }

    public Product getProduct(Integer productId) {

        try {
            String query = "SELECT p FROM Product p WHERE p.id = :productId";
            TypedQuery<Product> jpqlQuery = entityManager.createQuery(query, Product.class);
            jpqlQuery.setParameter("productId", (long)productId);

            return jpqlQuery.getSingleResult();
        } catch (NoResultException e) {
            //TODO : Exception 만들기

            return null;
        }
    }

    public void handleDummyStore(DummyStoreDto dummyStoreDto){
        List<Store> stores = new ArrayList<>();
        for (int i = 0; i < 10; i++) { // Create 500 products per DummyRequestDto
            Store store = Store.builder()
                    .storeName(dummyStoreDto.getStoreName())
                    .contact(dummyStoreDto.getContact())
                    .build();
            stores.add(store);
        }
        bulkInsert2(stores);
    }

    public void handleDummyInsertion(DummyRequestDto dummyRequestDto) {

        List<Product> products = new ArrayList<>();

        for (int i = 0; i < 100; i++) { // Create 500 products per DummyRequestDto
            Product product = Product.builder()
                    .imageUrl(dummyRequestDto.getImageUrl())
                    .animalCategory(dummyRequestDto.getAnimalCategory())
                    .productCategory(dummyRequestDto.getProductCategory())
                    .productName(dummyRequestDto.getProductName())
                    .price(dummyRequestDto.getPrice())
                    .description(dummyRequestDto.getDescription())
                    .stock(dummyRequestDto.getStock())
                    .wishCount(dummyRequestDto.getWishCount())
                    .createdAt(LocalDateTime.now())
                    .build();
            products.add(product);
        }

        bulkInsert(products);

    }

    public void bulkInsert(List<Product> products) {
        for (Product product : products) {
            entityManager.persist(product);
            if (product.getId() % 100 == 0) { // Flush and clear the persistence context every 50 records
                entityManager.flush();
                entityManager.clear();
            }
        }
    }

    public void bulkInsert2(List<Store> stores) {
        for (Store store : stores) {
            entityManager.persist(store);
            if (store.getId() % 100 == 0) { // Flush and clear the persistence context every 50 records
                entityManager.flush();
                entityManager.clear();
            }
        }
    }


    public Wish addWishList(Long userId, Long productId) {
        User validatedUser = validateUser(userId);
        Product validatedProduct = validateProduct(productId);

        if (existsInWishList(userId, productId)) {
            throw new ProductException(ProductErrorCode.WISHLIST_ALREADY_EXISTS);
        }

        return wishRepository.save(
                        Wish.builder()
                                .user(validatedUser)
                                .product(validatedProduct)
                                .build()

        );


    }

    public List<Wish> getWishList(int userId) {
        User validatedUser = validateUser((long)userId);
        return wishRepository.findByUser_Id(validatedUser.getId());
    }

    private User validateUser(Long userId){
        return userRepository.findById(userId)
                .orElseThrow(() -> new ProductException(ProductErrorCode.USER_NOT_FOUND));
    }

    private Product validateProduct(Long productId){
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductException(ProductErrorCode.THIS_PRODUCT_DOES_NOT_EXIST));

        return product;
    }

    private boolean existsInWishList(Long userId, Long productId){
        return wishRepository.existsByUserIdAndProductId(userId, productId);
    }


}
