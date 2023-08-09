package com.mc_website.ordersservice.datalayer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "orders")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Orders {
    @Id
    private String id;

    OrderIdentifier orderIdentifier;

    UserIdentifier user;

    List<Item> items;

    String message;

    public Orders(UserIdentifier user, List<Item> items, String message) {
        this.orderIdentifier = new OrderIdentifier();
        this.user = user;
        this.items = items;
        this.message = message;
    }
}
