//package com.supercoding.commerce03.web.dto.account;
//
//import com.supercoding.commerce03.repository.account.entity.Account;
//import lombok.*;
//
//import java.time.LocalDateTime;
//
//public class CreateAccount {
//
//    @Setter
//    @Getter
//    @AllArgsConstructor
//    @NoArgsConstructor
//    @Builder
//    public static class Request {
//
//        private String accountBy;
//        private Integer num;
//
//    }
//
//    @Setter
//    @Getter
//    @AllArgsConstructor
//    @NoArgsConstructor
//    @Builder
//    public static class Response {
//
//        private Long userId;
//        private Integer totalAccount;
//        private LocalDateTime createdAt;
//
//        public static Response from(Account account) {
//            return Response.builder()
//                    .userId(account.getUser().getId())
//                    .totalAccount(account.getTotalAccount())
//                    .createdAt(account.getCreatedAt())
//                    .build();
//        }
//
//
//    }
//}
