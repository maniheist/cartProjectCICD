package com.cart_project.cartProject.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Table(name = "payment")
public class Payment {
    @Id
    @NonNull
    private int paymentId;
    @NonNull
    private String currency;
    @NonNull
    private double amount;

    @ManyToOne
    @JoinColumn(name = "id")
    @JsonIgnore
    private User user;

}
