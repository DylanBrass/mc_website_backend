package com.mc_website.ordersservice.datalayer;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface OrdersRepository extends MongoRepository<Orders,Integer> {
    List<Orders> getOrdersByUser_UserId(String userId);

    Orders getOrdersByOrderIdentifier_OrderId(String orderId);

    boolean existsByOrderIdentifier_OrderId(String orderId);
}
