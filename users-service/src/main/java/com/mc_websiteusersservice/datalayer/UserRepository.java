package com.mc_websiteusersservice.datalayer;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findUserByUserIdentifier_UserId(String userId);

    User findByEmail(String email);

    User findByEmailAndPassword(String email, String password);

    boolean existsByUserIdentifier_UserId(String userId);



}
