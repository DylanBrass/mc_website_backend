package com.mc_website.apigateway.presentation.Customer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CustomerLoginRequestModel {
    String email;
    String password;
}
