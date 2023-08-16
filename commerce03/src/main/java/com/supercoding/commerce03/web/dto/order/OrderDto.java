package com.supercoding.commerce03.web.dto.order;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.util.List;

@Getter
public class OrderDto {

    @Getter
    @AllArgsConstructor
    @ToString
    public static class OrderProductRequest{
        private Long id;
        private Integer price;
        private Integer amount;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class OrderRegisterRequest{
        //받을 사람
        private String recipient;
        //받는 사람 주소
        private String address;
        //받는 사람 번호
        private String phoneNumber;
        //배송 요청 사항
        private String postComment;
        //주문 아이템들
        private List<OrderProductRequest> products;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class OrderProductResponse{
        private Long id;
        private String productName;
        private Integer price;
        private Integer amount;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class OrderRegisterResponse{
        private Long orderId;
        private String status;
        private Integer totalAmount;
        //받을 사람
        private String recipient;
        //받는 사람 주소
        private String address;
        //받는 사람 번호
        private String phoneNumber;
        //배송 요청 사항
        private String postComment;
        private List<OrderProductResponse> orderedProducts;


    }
}