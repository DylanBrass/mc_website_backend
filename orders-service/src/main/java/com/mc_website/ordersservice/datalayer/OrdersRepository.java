package com.mc_website.ordersservice.datalayer;

import com.mc_website.ordersservice.presentationlayer.OrderResponseModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface OrdersRepository extends MongoRepository<Orders,Integer> {
    List<Orders> getOrdersByCustomer_CustomerId(String customerId);

    Orders getOrdersByOrderIdentifier_OrderId(String orderId);
}
