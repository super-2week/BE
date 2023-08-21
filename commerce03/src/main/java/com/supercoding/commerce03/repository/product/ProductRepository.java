package com.supercoding.commerce03.repository.product;

import com.supercoding.commerce03.repository.product.entity.Product;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
//@Qualifier("master")
public interface ProductRepository extends JpaRepository<Product, Long> {

}
