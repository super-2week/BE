package com.supercoding.commerce03.web.controller.account;

import com.supercoding.commerce03.service.account.AccountService;
import com.supercoding.commerce03.web.dto.account.CreateAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Controller
@RequestMapping("/vi/api/account")
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CreateAccount.Request request){
        Long userId = 1L;
        return ResponseEntity.ok(accountService.createAccount(request, userId));
    }

}
