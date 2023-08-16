package com.supercoding.commerce03.service.product;

import com.supercoding.commerce03.repository.product.ProductRepository;
import com.supercoding.commerce03.repository.product.entity.Product;
import com.supercoding.commerce03.repository.store.entity.Store;
import com.supercoding.commerce03.web.dto.product.DummyRequestDto;
import com.supercoding.commerce03.web.dto.product.DummyStoreDto;
import com.supercoding.commerce03.web.dto.product.GetRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.relational.core.sql.In;
import org.springframework.stereotype.Service;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductService {

    @PersistenceContext
    private final EntityManager entityManager;
    private final ProductRepository productRepository;

    public List<Product> getProductsList(GetRequestDto getRequestDto, String searchWord) {

        String query =
                "SELECT p FROM Product p " +
                "WHERE p.animalCategory = :animalCategory " +
                "AND p.productCategory = :productCategory ";

        if(searchWord != null && !searchWord.isEmpty()){
            query += "AND p.productName LIKE :searchWord ";
        }

        if ("wishCount".equals(getRequestDto.getSortBy())) {
            //인기순
            query += "ORDER BY p.price ASC";
        } else if ("createdAt".equals(getRequestDto.getSortBy())) {
            //최신순
            query += "ORDER BY p.create_at ASC";
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
        List<Product> products = jpqlQuery.getResultList();

        return products;
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

    @Transactional
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

    @Transactional
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

    @Transactional
    public void bulkInsert(List<Product> products) {
        for (Product product : products) {
            entityManager.persist(product);
            if (product.getId() % 100 == 0) { // Flush and clear the persistence context every 50 records
                entityManager.flush();
                entityManager.clear();
            }
        }
    }

    @Transactional
    public void bulkInsert2(List<Store> stores) {
        for (Store store : stores) {
            entityManager.persist(store);
            if (store.getId() % 100 == 0) { // Flush and clear the persistence context every 50 records
                entityManager.flush();
                entityManager.clear();
            }
        }
    }
}
