package com.supercoding.commerce03.service.account;

import com.supercoding.commerce03.repository.account.AccountRepository;
import com.supercoding.commerce03.repository.account.entity.Account;
import com.supercoding.commerce03.repository.account.entity.AccountByType;
import com.supercoding.commerce03.repository.user.UserRepository;
import com.supercoding.commerce03.repository.user.entity.User;
import com.supercoding.commerce03.web.dto.account.CreateAccount;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
@Slf4j
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;


    public CreateAccount.Response createAccount(CreateAccount.Request request, Long userId) {
        String accountBy = request.getAccountBy();
//        if (accountBy )
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("멤버가 없습니다."));

        return CreateAccount.Response.from(accountRepository.save(Account.builder()
                .accountBy(AccountByType.CARD)
                .user(user)
                .num(request.getNum())
                .totalAccount(0)
                .createdAt(LocalDateTime.now())
                .build()));
    }

}
