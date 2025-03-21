package com.cart_project.cartProject.repository;

import com.cart_project.cartProject.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment,Integer> {
}
