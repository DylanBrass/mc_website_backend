package com.mc_websiteusersservice.datalayer;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ResetPasswordTokenRepository extends JpaRepository<ResetPasswordToken, Integer> {
    ResetPasswordToken findResetPasswordTokenByToken(String token);
    ResetPasswordToken findResetPasswordTokenByUserIdentifier_UserId(String userId);
}
