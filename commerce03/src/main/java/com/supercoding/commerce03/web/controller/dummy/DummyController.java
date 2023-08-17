package com.supercoding.commerce03.web.controller.dummy;

import com.supercoding.commerce03.repository.product.entity.Product;
import com.supercoding.commerce03.repository.store.entity.Store;
import com.supercoding.commerce03.service.product.ProductService;
import com.supercoding.commerce03.web.dto.product.DummyRequestDto;
import com.supercoding.commerce03.web.dto.product.DummyStoreDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@Transactional
public class DummyController {
    private final EntityManager entityManager;
    private final List<Store> storeBatch = new ArrayList<>();
    private final List<Product> productBatch = new ArrayList<>();
    private static final int BATCH_SIZE = 100; //엔티티 100개씩 모아서 bulkInsert를 합니다.

    @PostMapping("/dummy")
    @ResponseBody
    public ResponseEntity createDummy(@RequestBody DummyRequestDto dummyRequestDto) {
        try {
            List<Product> products = new ArrayList<>();

            for (int i = 0; i < 100; i++) { // Create 100 products per DummyRequestDto
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
            bulkInsertProduct(products);
            return new ResponseEntity<>("Success", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/dummy/store")
    @ResponseBody
    public ResponseEntity createDummyStore(@RequestBody DummyStoreDto dummyStoreDto) {
        System.out.println(dummyStoreDto);
        try {

            // Create 500 products per DummyRequestDto
            Store store = Store.builder()
                        .storeName(dummyStoreDto.getStoreName())
                        .contact(dummyStoreDto.getContact())
                        .build();

            storeBatch.add(store);
            if (storeBatch.size() >= BATCH_SIZE) {
                bulkInsertStore();
            }



            return new ResponseEntity<>("Success", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public void bulkInsertProduct(List<Product> products) {
//        for (Product product : products) {
//            entityManager.persist(product);
//        }
//        String query =
//                "INSERT INTO Product (imageUrl, animalCategory, productCategory, productName, price, description, stock, wishCount, createdAt) " +
//                "VALUES (:imageUrl, :animalCategory, :productCategory, :productName, :price, :description, :stock, :wishCount, :createdAt)";
//
//        Query jpqlQuery = entityManager.createQuery(query, Product.class);
//
//        for (Product product : products) {
//            jpqlQuery.setParameter("imageUrl", product.getImageUrl());
//            jpqlQuery.setParameter("animalCategory", product.getAnimalCategory());
//            jpqlQuery.setParameter("productCategory", product.getProductCategory());
//            jpqlQuery.setParameter("productName", product.getProductName());
//            jpqlQuery.setParameter("price", product.getPrice());
//            jpqlQuery.setParameter("description", product.getDescription());
//            jpqlQuery.setParameter("stock", product.getStock());
//            jpqlQuery.setParameter("wishCount", product.getWishCount());
//            jpqlQuery.setParameter("createdAt", product.getCreatedAt());
//
//            jpqlQuery.executeUpdate();
//        }
        Session session = entityManager.unwrap(Session.class);

        for (int i = 0; i < products.size(); i++) {
            session.saveOrUpdate(products.get(i));

            if (i % 50 == 0) { // Flush and clear the session every 50 records
                session.flush();
                session.clear();
            }

        }
    }

        public void bulkInsertStore () {

            Session session = entityManager.unwrap(Session.class);
            for (int i = 0; i < storeBatch.size(); i++) {
                session.saveOrUpdate(storeBatch.get(i));
            }

            session.flush();
            session.clear();
            storeBatch.clear();

        }

}



