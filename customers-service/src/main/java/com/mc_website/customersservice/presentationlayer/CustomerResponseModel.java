package com.mc_website.customersservice.presentationlayer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CustomerResponseModel {
    String customerId;
    String firstName;
    String lastName;
    String email;
    String phoneNumber;
    String password;
}
