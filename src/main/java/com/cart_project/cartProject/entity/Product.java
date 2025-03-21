package com.cart_project.cartProject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product")
public class Product {
    @Id
    @Column(name = "productId")
    private int id;
    @Column(name = "productName")
    private String name;
    @Column(name = "productDesc")
    private String description;
    @Column(name = "productPrice")
    private double price;
    @Column(name = "productStock")
    private int stock;

}