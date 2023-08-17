package com.supercoding.commerce03.service.order.mapper;

import com.supercoding.commerce03.repository.order.entity.Order;
import com.supercoding.commerce03.repository.order.entity.OrderDetail;
import com.supercoding.commerce03.repository.product.entity.Product;
import com.supercoding.commerce03.repository.user.entity.User;
import com.supercoding.commerce03.web.dto.order.OrderDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

@Mapper
public interface OrderMapper {
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    @Mapping(target="order", source = "order")
    @Mapping(target="amount", source="amount")
    @Mapping(target="isDeleted", expression = "java(false)")
    @Mapping(target="product", source = "product")
    @Mapping(target="id", ignore = true)
    OrderDetail productToOrderDetail(Product product, Order order, Integer amount);
}
