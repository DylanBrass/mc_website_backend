package com.mc_website.apigateway.businesslayer.Orders;

import com.mc_website.apigateway.presentation.Orders.OrderRequestModel;
import com.mc_website.apigateway.presentation.Orders.OrderResponseModel;



public interface OrderService {
    OrderResponseModel addOrder(OrderRequestModel orderRequestModel, String customerId);
    OrderResponseModel[] getAllOrders();

    OrderResponseModel[] getAllOrdersForCustomer(String customerId);
    OrderResponseModel getOrderById(String orderId);
    OrderResponseModel updateOrder(OrderRequestModel orderRequestModel,String orderId);
    void deleteOrder(String orderId);
}
