package com.supercoding.commerce03.service.product;

import com.supercoding.commerce03.repository.product.ProductRepository;
import com.supercoding.commerce03.repository.product.entity.Product;
import com.supercoding.commerce03.repository.store.entity.Store;
import com.supercoding.commerce03.web.dto.product.DummyRequestDto;
import com.supercoding.commerce03.web.dto.product.DummyStoreDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final EntityManager entityManager;
    private final ProductRepository productRepository;

    public List<Product> getProductsList(Integer animalCategory, Integer productCategory, Integer sortBy, String searchWord) {
    return null;
    }

    public Product getProduct() {
        return null;
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
                    .created_at(LocalDateTime.now())
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
