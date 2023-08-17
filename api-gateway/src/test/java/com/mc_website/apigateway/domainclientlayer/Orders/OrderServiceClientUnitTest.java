package com.mc_website.apigateway.domainclientlayer.Orders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mc_website.apigateway.presentation.Orders.Item;
import com.mc_website.apigateway.presentation.Orders.ItemType;
import com.mc_website.apigateway.presentation.Orders.OrderResponseModel;
import com.mc_website.apigateway.presentation.Orders.OrderType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.mockito.Mockito.when;

class OrderServiceClientUnitTest {

    private RestTemplate restTemplate;
    private ObjectMapper objectMapper;
    private OrderServiceClient orderServiceClient;
    private String ORDER_SERVICE_BASE_URL;

    @BeforeEach
    public void setup(){
        restTemplate = Mockito.mock(RestTemplate.class);
        orderServiceClient = new OrderServiceClient(restTemplate, objectMapper, "localhost", "8080");
        this.objectMapper = new ObjectMapper();
        ORDER_SERVICE_BASE_URL = "http://localhost:8080/api/v1/users";
    }

    @Test
    void getAllOrders() {
        List<Item> items = new ArrayList<>();
        Item item1 = Item.builder()
                .item("item1")
                .itemType(ItemType.RPG)
                .description("description1")
                .orderType(OrderType.ORDER)
                .quantity(1)
                .build();

        Item item2 = Item.builder()
                .item("item2")
                .itemType(ItemType.MTG)
                .description("description2")
                .orderType(OrderType.PRE_ORDER)
                .quantity(2)
                .build();

        items.add(item1);
        items.add(item2);

        OrderResponseModel[] orderResponseModels = new OrderResponseModel[2];
        orderResponseModels[0] = OrderResponseModel.builder()
                .orderId("123")
                .user("111111")
                .items(items)
                .message("message1")
                .build();

        when(restTemplate.getForObject(ORDER_SERVICE_BASE_URL + "/orders", OrderResponseModel[].class))
                .thenReturn(orderResponseModels);

        OrderResponseModel[] actualOrderResponseModels = orderServiceClient.getAllOrders();
        assertArrayEquals(orderResponseModels, actualOrderResponseModels);
    }

}