package com.mc_website.customersservice.datalayer;

import com.mc_website.customersservice.presentationlayer.CustomerResponseModel;
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
    private String password;


    //add a when the resetPasswordToken was created time to make it expire
    public Customer(){
        this.customerIdentifier = new CustomerIdentifier();
    }

    public Customer(Integer id, CustomerIdentifier customerIdentifier, String firstName, String lastName, String email, String phoneNumber, String password) {
        Id = id;
        this.customerIdentifier = customerIdentifier;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    public Customer(CustomerResponseModel customer) {
        this.customerIdentifier = new CustomerIdentifier();
        this.firstName = customer.getFirstName();
        this.lastName = customer.getLastName();
        this.email = customer.getEmail();
        this.phoneNumber = customer.getPhoneNumber();
    }
}
