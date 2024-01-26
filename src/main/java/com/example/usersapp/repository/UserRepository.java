package com.example.usersapp.repository;

import com.example.usersapp.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("SELECT u FROM User u WHERE LOWER(u.name) LIKE %:value% OR LOWER(u.surname) LIKE %:value% OR LOWER(u.login) LIKE %:value%")
    Page<User> findByValue(String value, Pageable pageable);
}
