package com.cart_project.cartProject.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name ="credentials")
@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class User {
    @Id
    @NonNull
    private int id;
    @NonNull
    private String name;
    @NonNull
    private String email;
    @NonNull
    private String password;
    @NonNull
    private String role;

    private String country;

    @OneToMany(mappedBy = "user")
    private List<Payment> paymentList;

}
