package com.mc_website.apigateway.presentation.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserRequestModel {
    String firstName;
    String lastName;
    String email;
    String phoneNumber;
    String password;
}
