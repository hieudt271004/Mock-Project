package com.hieudt79.user_service.repository;

import com.hieudt79.user_service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {

    User findById(long id);

    User findByFullName(String name);

    void deleteByUserId(Long id);

    User findByEmail(String email);

    List<User> findAll();

}