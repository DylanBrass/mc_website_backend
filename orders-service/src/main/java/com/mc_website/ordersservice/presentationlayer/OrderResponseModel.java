package com.mc_website.ordersservice.presentationlayer;

import com.mc_website.ordersservice.datalayer.Item;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderResponseModel {
    String orderId;
    String user;
    List<Item> items;
    String message;
}
