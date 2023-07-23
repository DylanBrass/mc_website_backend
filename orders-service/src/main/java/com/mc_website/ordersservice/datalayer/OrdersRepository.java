package com.mc_website.ordersservice.datalayer;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrdersRepository extends MongoRepository<Orders,Integer> {
    
}
