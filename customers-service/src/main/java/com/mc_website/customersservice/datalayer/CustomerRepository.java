package com.mc_website.customersservice.datalayer;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    Customer findByCustomerIdentifier_CustomerId(String customerId);

    Customer findByEmail(String email);
    boolean existsByCustomerIdentifier_CustomerId(String customerId);

}
