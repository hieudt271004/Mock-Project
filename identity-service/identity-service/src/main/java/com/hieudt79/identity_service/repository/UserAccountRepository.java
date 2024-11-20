package com.hieudt79.identity_service.repository;

import com.hieudt79.identity_service.model.UserAccount;
import com.hieudt79.identity_service.utils.enums.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {

    boolean existsByEmail(String email);

    Optional<UserAccount> findByEmail(String email);

    @Query(value = "select r.roleName from Role r inner join UserRole ur on r.roleId = ur.userRoleId where ur.userRoleId= :userId")
    List<String> findAllRoleByUserId(long userId);

    List<UserAccount> findAll();

    List<UserAccount> findAllByStatus(UserStatus status);
}
