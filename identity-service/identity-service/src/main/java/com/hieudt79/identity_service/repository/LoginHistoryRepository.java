package com.hieudt79.identity_service.repository;

import com.hieudt79.identity_service.model.LoginHistory;
import com.hieudt79.identity_service.model.UserAccount;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface LoginHistoryRepository extends JpaRepository<LoginHistory, Long> {

    Optional<LoginHistory> findByUserAccount(UserAccount account);
}
