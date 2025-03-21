package com.cart_project.cartProject.repository;

import com.cart_project.cartProject.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface ProductRepository extends JpaRepository<Product,Integer> {
Optional<Product> findPriceById(int id);
List<Product> findProductByName(String productName);
Page<Product> findByNameContainingIgnoreCase(Pageable pageable, String name);
}
