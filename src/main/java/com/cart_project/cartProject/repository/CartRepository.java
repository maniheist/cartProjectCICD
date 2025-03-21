package com.cart_project.cartProject.repository;

import com.cart_project.cartProject.entity.Cart;
import com.cart_project.cartProject.entity.User;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {
    Optional<Cart> findByUserIdAndProductId(int userId,int productId);
    List<Cart> findByUserId(int userId);
}
