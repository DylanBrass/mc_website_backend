package com.mc_website.apigateway.businesslayer.Orders;

import com.mc_website.apigateway.presentation.Orders.OrderRequestModel;
import com.mc_website.apigateway.presentation.Orders.OrderResponseModel;


import java.util.List;

public interface OrderService {
    OrderResponseModel addOrder(OrderRequestModel orderRequestModel, String customerId);
    OrderResponseModel[] getAllOrders();

    OrderResponseModel[] getAllOrdersForCustomer(String customerId);
}
