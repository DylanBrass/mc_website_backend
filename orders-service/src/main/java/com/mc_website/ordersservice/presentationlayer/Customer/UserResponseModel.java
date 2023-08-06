package com.mc_website.ordersservice.presentationlayer.Customer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserResponseModel {
    String userId;
    String firstName;
    String lastName;
    String email;
    String phoneNumber;
    String password;
}
