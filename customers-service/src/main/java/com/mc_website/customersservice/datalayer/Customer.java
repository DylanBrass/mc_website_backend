package com.mc_website.customersservice.datalayer;

import jakarta.persistence.*;
import lombok.Data;

@Table(name="customers")
@Entity
@Data
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;
    @Embedded
    private CustomerIdentifier customerIdentifier;

    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;

    public Customer(){
        this.customerIdentifier = new CustomerIdentifier();
    }

    public Customer(String firstName, String lastName, String email, String phoneNumber) {
        this.customerIdentifier = new CustomerIdentifier();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }
}
