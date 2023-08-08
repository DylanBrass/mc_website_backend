package com.mc_website.ordersservice.datalayer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Item {
    String item;
    ItemType itemType;
    String description;
    OrderType orderType;
    Integer quantity;


}
