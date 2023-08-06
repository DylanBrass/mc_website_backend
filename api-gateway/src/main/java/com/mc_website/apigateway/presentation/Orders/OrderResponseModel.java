package com.mc_website.apigateway.presentation.Orders;

import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderResponseModel {
    String orderId;
    String user;
    List<Item> items;
    String message;
}
