package com.mc_website.apigateway.presentation.Orders;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
