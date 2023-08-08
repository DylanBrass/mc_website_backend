package com.mc_website.apigateway.presentation.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserResponseModel {
    String userId;
    String firstName;
    String lastName;
    @JsonIgnore
    String password;
    String email;
    String phoneNumber;
}
