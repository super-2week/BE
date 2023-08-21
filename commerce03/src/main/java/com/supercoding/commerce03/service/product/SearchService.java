package com.supercoding.commerce03.service.product;

import com.supercoding.commerce03.repository.product.ProductRepository;
import com.supercoding.commerce03.repository.product.entity.Product;
import com.supercoding.commerce03.web.dto.product.ProductDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.sql.DataSource;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SearchService {
    private final DataSource dataSource;
    private final ProductRepository productRepository;
    private final EntityManager entityManager;

    @Transactional(readOnly = true)
    public List<ProductDto> searchFullText(){
        Long productId = 1000L;
        String query =
                "SELECT NEW com.supercoding.commerce03.web.dto.product.ProductDto(" +
                        "p.id, p.imageUrl, p.animalCategory, p.productCategory, p.productName, " +
                        "p.modelNum, p.originLabel, p.price, p.description, p.stock, p.wishCount, p.purchaseCount, p.createdAt, p.store" +
                        ") " +
                        "FROM Product p WHERE p.id = :productId";
        TypedQuery<ProductDto> jpqlQuery = entityManager.createQuery(query, ProductDto.class);
        jpqlQuery.setParameter("productId", productId);

        return jpqlQuery.getResultList();
    }

}

