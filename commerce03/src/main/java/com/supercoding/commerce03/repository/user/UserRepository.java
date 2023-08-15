package com.supercoding.commerce03.repository.user;

import com.supercoding.commerce03.repository.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
