package com.mc_website.ordersservice.businesslayer;

import com.mc_website.ordersservice.presentationlayer.OrderRequestModel;
import com.mc_website.ordersservice.presentationlayer.OrderResponseModel;

import javax.mail.MessagingException;
import java.util.List;

public interface OrderService {
    OrderResponseModel addOrder(OrderRequestModel orderRequestModel, String customerId) throws MessagingException;
    List<OrderResponseModel> getAllOrders();

    List<OrderResponseModel> getAllOrdersForCustomer(String customerId);
}
