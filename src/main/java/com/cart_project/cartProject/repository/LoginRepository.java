package com.cart_project.cartProject.repository;

import com.cart_project.cartProject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoginRepository extends JpaRepository<User,Integer> {
    Optional<User> findByEmailAndPassword(String name, String password);
    Optional<User> findByEmail(String email);
}
