package com.mc_website.apigateway.businesslayer.Orders;

import com.mc_website.apigateway.domainclientlayer.Orders.OrderServiceClient;
import com.mc_website.apigateway.presentation.Orders.OrderRequestModel;
import com.mc_website.apigateway.presentation.Orders.OrderResponseModel;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class OrderServiceImpl implements OrderService{

    OrderServiceClient orderServiceClient;

    public OrderServiceImpl(OrderServiceClient orderServiceClient) {
        this.orderServiceClient = orderServiceClient;
    }

    @Override
    public OrderResponseModel addOrder(OrderRequestModel orderRequestModel,String customerId) {
        return orderServiceClient.addOrder(orderRequestModel,customerId);
    }

    @Override
    public OrderResponseModel[] getAllOrders() {
        return orderServiceClient.getAllOrders();
    }

    @Override
    public OrderResponseModel[] getAllOrdersForCustomer(String customerId) {
        return orderServiceClient.getAllCustomersOrders(customerId);
    }
}
