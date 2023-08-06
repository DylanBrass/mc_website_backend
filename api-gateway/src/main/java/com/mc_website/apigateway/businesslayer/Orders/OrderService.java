package com.mc_website.apigateway.businesslayer.Orders;

import com.mc_website.apigateway.presentation.Orders.OrderRequestModel;
import com.mc_website.apigateway.presentation.Orders.OrderResponseModel;



public interface OrderService {
    OrderResponseModel addOrder(OrderRequestModel orderRequestModel, String userId);
    OrderResponseModel[] getAllOrders();

    OrderResponseModel[] getAllOrdersForUser(String userId);
    OrderResponseModel getOrderById(String orderId);
    OrderResponseModel updateOrder(OrderRequestModel orderRequestModel,String orderId);
    void deleteOrder(String orderId);
}
