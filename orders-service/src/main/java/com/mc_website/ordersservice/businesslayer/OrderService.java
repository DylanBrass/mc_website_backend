package com.mc_website.ordersservice.businesslayer;

import com.mc_website.ordersservice.presentationlayer.OrderRequestModel;
import com.mc_website.ordersservice.presentationlayer.OrderResponseModel;

import javax.mail.MessagingException;
import java.util.List;

public interface OrderService {
    OrderResponseModel addOrder(OrderRequestModel orderRequestModel, String userId) throws MessagingException;
    List<OrderResponseModel> getAllOrders();
    List<OrderResponseModel> getAllOrdersForUser(String userId);
    OrderResponseModel getOrderById(String orderId);
    OrderResponseModel updateOrder(OrderRequestModel orderRequestModel,String orderId) throws MessagingException;
    void deleteOrder(String orderId);

}
