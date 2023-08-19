package com.supercoding.commerce03.repository.user;

import com.supercoding.commerce03.repository.user.entity.UserDetail;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDetailRepository extends JpaRepository<UserDetail, Long> {

    boolean existsByEmail(String email);

    Optional<UserDetail> findByEmail(String email);

    UserDetail findByUserId(Long userId);

}
