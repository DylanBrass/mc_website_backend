package com.mc_website.ordersservice.datalayer;

import com.mc_website.ordersservice.presentationlayer.OrderResponseModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface OrdersRepository extends MongoRepository<Orders,Integer> {
    List<Orders> getOrdersByUser_UserId(String userId);

    Orders getOrdersByOrderIdentifier_OrderId(String orderId);
}
