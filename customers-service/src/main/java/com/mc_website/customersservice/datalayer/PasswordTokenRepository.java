package com.mc_website.customersservice.datalayer;

import com.mc_website.customersservice.presentationlayer.CustomerRequestModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.stream.Stream;

public interface PasswordTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    /*PasswordResetToken findByToken(String token);

    PasswordResetToken findByUser(CustomerRequestModel customer);

    Stream<PasswordResetToken> findAllByExpiryDateLessThan(LocalDate now);

    void deleteByExpiryDateLessThan(LocalDate now);

    @Modifying
    @Query("delete from PasswordResetToken t where t.expiryDate <= ?1")
    void deleteAllExpiredSince(LocalDate now);*/
}