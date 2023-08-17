//package com.supercoding.commerce03.web.controller.account;
//
//import com.supercoding.commerce03.service.account.AccountService;
//import com.supercoding.commerce03.web.dto.account.ChargeAccount;
//import com.supercoding.commerce03.web.dto.account.CreateAccount;
//import com.supercoding.commerce03.web.dto.account.InquiryAccountResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RequiredArgsConstructor
//@Controller
//@RequestMapping("/v1/api/account")
//public class AccountController {
//
//    private final AccountService accountService;
//
//    @PostMapping
//    public ResponseEntity<?> create(@RequestBody CreateAccount.Request request) {
//        Long userId = 1L;
//        return ResponseEntity.ok(accountService.createAccount(request, userId));
//    }
//
//    @GetMapping("/list")
//    public List<InquiryAccountResponse.Response> findByAccountId(Long user_id) {
//
//        return accountService.findByAccountId(user_id);
//    }
//
////    @PutMapping("/{id}")
////    public ChargeAccount.Response charge(@PathVariable Long user_id, @RequestBody ChargeAccount.Request request){
////        return accountService.charge(request, user_id);
////    }
//
//    @PatchMapping("/charge")
//    public ChargeAccount.Response chargeBy(@RequestBody ChargeAccount.Request request){
//        Long userId = 1L;
//        return accountService.chargeTotalAccount(userId,request);
//    }
//
//
//}
