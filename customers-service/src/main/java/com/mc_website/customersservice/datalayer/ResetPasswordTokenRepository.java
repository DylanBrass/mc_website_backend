package com.mc_website.customersservice.datalayer;

import org.apache.el.parser.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResetPasswordTokenRepository extends JpaRepository<ResetPasswordToken, Integer> {
    ResetPasswordToken findResetPasswordTokenByToken(String token);
    ResetPasswordToken findResetPasswordTokenByCustomerIdentifier_CustomerId(String customerId);
}
