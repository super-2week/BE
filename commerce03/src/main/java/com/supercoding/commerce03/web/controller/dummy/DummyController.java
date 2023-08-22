package com.supercoding.commerce03.web.controller.dummy;

import com.supercoding.commerce03.repository.product.entity.Product;
import com.supercoding.commerce03.repository.store.StoreRepository;
import com.supercoding.commerce03.repository.store.entity.Store;
import com.supercoding.commerce03.web.dto.dummy.DummyRequestDto;
import com.supercoding.commerce03.web.dto.dummy.DummyStoreDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
//@Transactional
public class DummyController {
    private final StoreRepository storeRepository;
    private final EntityManager entityManager;
    private final List<Store> storeBatch = new ArrayList<>();
    private final List<Product> productBatch = new ArrayList<>();
    private static final int BATCH_SIZE = 100; //엔티티 100개씩 모아서 bulkInsert를 합니다.

    @PostMapping("/dummy")
    @ResponseBody
    @Transactional
    public ResponseEntity createDummy(
//            HttpServletRequest request
            @RequestBody Map<String, List<DummyRequestDto>> json
    ) {
        try {
//            StringBuilder requestBody = new StringBuilder();
//            BufferedReader reader = request.getReader();
//            String line;
//            while ((line = reader.readLine()) != null) {
//                requestBody.append(line);
//            }
//            System.out.println("Request Body: " + requestBody.toString());
            List<DummyRequestDto> dtoList = json.get("products");

            for (DummyRequestDto dto : dtoList) { // Create 100 products per DummyRequestDto
                Store store = storeRepository.findById((long)dto.getStoreId()).orElse(null);
                Product product = Product.builder()
                        .store(store)
                        .imageUrl(dto.getImageUrl())
                        .animalCategory(dto.getAnimalCategory())
                        .productCategory(dto.getProductCategory())
                        .productName(dto.getProductName())
                        .modelNum(dto.getModelNum())
                        .originLabel(dto.getOriginLabel())
                        .price(dto.getPrice())
                        .description(dto.getDescription())
                        .stock(dto.getStock())
                        .wishCount(dto.getWishCount())
                        .purchaseCount(dto.getPurchaseCount())
                        .createdAt(LocalDateTime.now())
                        .build();
                productBatch.add(product);
            }
            if (productBatch.size() >= BATCH_SIZE) {
                bulkInsertProduct();
            }
            return new ResponseEntity<>("Success", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/dummy/store")
    @ResponseBody
    @Transactional
    public ResponseEntity createDummyStore(@RequestBody DummyStoreDto dummyStoreDto) {

        try {
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


    public void bulkInsertProduct() {

        Session session = entityManager.unwrap(Session.class);
        for (int i = 0; i < productBatch.size(); i++) {
            session.saveOrUpdate(productBatch.get(i));
        }

        session.flush();
        session.clear();
        productBatch.clear();
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



