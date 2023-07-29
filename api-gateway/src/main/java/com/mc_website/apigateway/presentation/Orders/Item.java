package com.mc_website.apigateway.presentation.Orders;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    String item;
    ItemType itemType;
    String description;
    OrderType orderType;
    Integer quantity;


}
