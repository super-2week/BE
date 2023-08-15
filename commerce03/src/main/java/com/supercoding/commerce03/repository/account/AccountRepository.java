package com.supercoding.commerce03.repository.account;

import com.supercoding.commerce03.repository.account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {

}
