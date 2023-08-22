package com.supercoding.commerce03.repository.product;

import com.supercoding.commerce03.repository.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

//Full Text Index
    @Query(value =
            "SELECT DISTINCT product_name FROM products p" +
                    " WHERE MATCH(p.product_name) AGAINST(?1 IN BOOLEAN MODE)", nativeQuery = true)

    //B-Tree Index
//    @Query(value =
//            "SELECT DISTINCT product_name FROM products p" +
//                    " WHERE p.product_name LIKE ?1", nativeQuery = true)
    List<Object[]> fullTextSearch(String searchWord);

}
