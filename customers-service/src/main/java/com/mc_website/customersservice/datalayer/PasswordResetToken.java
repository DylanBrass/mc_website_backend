package com.mc_website.customersservice.datalayer;

import com.mc_website.customersservice.presentationlayer.CustomerResponseModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PasswordResetToken {

    private static final int EXPIRATION = 60 * 24;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String token;

    @OneToOne(targetEntity = Customer.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "customer_id")
    private Customer customer;

    private LocalDate expiryDate;


    public PasswordResetToken(String token, CustomerResponseModel customer) {
        this.token = token;
        this.customer = new Customer(customer);
    }
}
