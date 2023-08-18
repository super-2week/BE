package com.supercoding.commerce03.repository.order.entity;

import com.supercoding.commerce03.repository.product.entity.Product;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductAndOrderAmount {
    private Product product;
    private Integer amount;
}
