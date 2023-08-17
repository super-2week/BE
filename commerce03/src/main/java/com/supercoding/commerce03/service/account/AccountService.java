//package com.supercoding.commerce03.service.account;
//
//import com.supercoding.commerce03.repository.account.AccountRepository;
//import com.supercoding.commerce03.repository.account.entity.Account;
//import com.supercoding.commerce03.repository.account.entity.AccountByType;
//import com.supercoding.commerce03.repository.user.UserRepository;
//import com.supercoding.commerce03.repository.user.entity.User;
//import com.supercoding.commerce03.web.dto.account.ChargeAccount;
//import com.supercoding.commerce03.web.dto.account.CreateAccount;
//import com.supercoding.commerce03.web.dto.account.InquiryAccountResponse;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@RequiredArgsConstructor
//@Service
//@Slf4j
//public class AccountService {
//
//    private final AccountRepository accountRepository;
//    private final UserRepository userRepository;
//
//
//    public CreateAccount.Response createAccount(CreateAccount.Request request, Long userId) {
//        String accountBy = request.getAccountBy();
////        if (accountBy )
//        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("멤버가 없습니다."));
//
//        return CreateAccount.Response.from(accountRepository.save(Account.builder()
//                .accountBy(AccountByType.CARD)
//                .user(user)
//                .num(request.getNum())
//                .totalAccount(0)
//                .createdAt(LocalDateTime.now())
//                .build()));
//    }
//
//    public List<InquiryAccountResponse.Response> findByAccountId(Long userId) {
//        List<Account> accounts = accountRepository.findAllById(userId);
//        return accounts.stream()
//                .map(account -> InquiryAccountResponse.Response.builder()
//                        .userId(userId)
//                        .totalAccount(account.getTotalAccount())
//                        .createdAt(LocalDateTime.now())
//                        .num(account.getNum())
//                        .accountBy(account.getAccountBy())
//                        .build())
//                .collect(Collectors.toList());
//    }
//
////    @Transactional
////    public ChargeAccount.Response charge(ChargeAccount.Request request, Long user_id) {
//////        User user = userRepository.findById(user_id).orElseThrow(()->new RuntimeException("존재하지 않는 id입니다."));
//////        String accountBy = request.getAccountBy();
////        Integer totalAccount = request.getTotalAccount();
//////        Account account = Account.set(totalAccount);
////        accountRepository.findById(user_id).orElseThrow(()->new RuntimeException("존재하지 않는 id입니다."));
////        return ChargeAccount.Response.builder()
////                .userId(user_id)
////                .totalAccount(request.getTotalAccount())
////                .createdAt(LocalDateTime.now())
////                .build();
////    }
//
//    @Transactional
//    public ChargeAccount.Response chargeTotalAccount(Long userId, ChargeAccount.Request request){
//        Account account = accountRepository.findById(request.getAccountId()).orElseThrow(() -> new RuntimeException("멤버가 없습니다."));
//        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("멤버가 없습니다."));
//        int updateTotalAccount = account.getTotalAccount() + request.getTotalAccount();
//        account.setTotalAccount(updateTotalAccount);
//
//        return ChargeAccount.Response.from(account);
//
//    }
//
//}
