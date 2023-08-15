package com.supercoding.commerce03.repository.product;

import com.supercoding.commerce03.repository.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
