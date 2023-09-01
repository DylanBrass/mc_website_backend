package com.mc_website.apigateway.businesslayer.Orders;

import com.mc_website.apigateway.domainclientlayer.Orders.OrderServiceClient;
import com.mc_website.apigateway.presentation.Orders.OrderRequestModel;
import com.mc_website.apigateway.presentation.Orders.OrderResponseModel;
import org.springframework.stereotype.Service;
@Service
public class OrderServiceImpl implements OrderService{

    OrderServiceClient orderServiceClient;

    public OrderServiceImpl(OrderServiceClient orderServiceClient) {
        this.orderServiceClient = orderServiceClient;
    }

    @Override
    public OrderResponseModel addOrder(OrderRequestModel orderRequestModel,String userId) {
        return orderServiceClient.addOrder(orderRequestModel,userId);
    }

    @Override
    public OrderResponseModel[] getAllOrders() {
        return orderServiceClient.getAllOrders();
    }

    @Override
    public OrderResponseModel[] getAllOrdersForUser(String userId) {
        return orderServiceClient.getAllUsersOrders(userId);
    }

    @Override
    public OrderResponseModel getOrderById(String orderId) {
        OrderResponseModel[] allOrders = orderServiceClient.getAllOrders();
        for(OrderResponseModel orders: allOrders){
           if(orders.getOrderId().equals(orderId)){
               return orders;
           }
        }
        return null;
    }

    @Override
    public OrderResponseModel updateOrder(OrderRequestModel orderRequestModel, String orderId) {
        return orderServiceClient.updateOrder(orderRequestModel,orderId);
    }

    @Override
    public void deleteOrder(String orderId) {
        deleteOrder(orderId);
    }
}
