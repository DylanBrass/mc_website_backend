package com.mc_website.ordersservice.businesslayer;

import com.mc_website.ordersservice.presentationlayer.OrderRequestModel;
import com.mc_website.ordersservice.presentationlayer.OrderResponseModel;

import java.util.List;

public interface OrderService {
    OrderResponseModel addOrder(OrderRequestModel orderRequestModel);
    List<OrderResponseModel> getAllOrders();
}
