package com.mc_website.apigateway.domainclientlayer.Orders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mc_website.apigateway.presentation.Orders.*;
import com.mc_website.apigateway.utils.exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.NOT_FOUND;

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

    @Test
    void getAllOrders_whenRestTemplateThrowsHttpClientErrorExceptionWithStatusNotFound_thenThrowNotFoundException() {
        when(restTemplate.getForObject(ORDER_SERVICE_BASE_URL + "/orders", OrderResponseModel[].class))
                .thenThrow(new HttpClientErrorException(NOT_FOUND));

        assertThrows(NotFoundException.class, () -> orderServiceClient.getAllOrders());
    }

    @Test
    void getAllOrders_whenRestTemplateThrowsHttpClientErrorExceptionWithStatusUnprocessableEntity_thenThrowInvalidInputException() {
        when(restTemplate.getForObject(ORDER_SERVICE_BASE_URL + "/orders", OrderResponseModel[].class))
                .thenThrow(new HttpClientErrorException(NOT_FOUND));

        assertThrows(NotFoundException.class, () -> orderServiceClient.getAllOrders());
    }

    @Test
    void getAllUsersOrders() {
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

        when(restTemplate.getForObject(ORDER_SERVICE_BASE_URL + "/111111/orders", OrderResponseModel[].class))
                .thenReturn(orderResponseModels);

        OrderResponseModel[] actualOrderResponseModels = orderServiceClient.getAllUsersOrders("111111");
        assertArrayEquals(orderResponseModels, actualOrderResponseModels);
    }

    @Test
    void getAllUsersOrders_whenRestTemplateThrowsHttpClientErrorExceptionWithStatusNotFound_thenThrowNotFoundException() {
        when(restTemplate.getForObject(ORDER_SERVICE_BASE_URL + "/111111/orders", OrderResponseModel[].class))
                .thenThrow(new HttpClientErrorException(NOT_FOUND));

        assertThrows(NotFoundException.class, () -> orderServiceClient.getAllUsersOrders("111111"));
    }

    @Test
    void getAllUsersOrders_whenRestTemplateThrowsHttpClientErrorExceptionWithStatusUnprocessableEntity_thenThrowInvalidInputException() {
        when(restTemplate.getForObject(ORDER_SERVICE_BASE_URL + "/111111/orders", OrderResponseModel[].class))
                .thenThrow(new HttpClientErrorException(NOT_FOUND));

        assertThrows(NotFoundException.class, () -> orderServiceClient.getAllUsersOrders("111111"));
    }

    @Test
void addOrder() {
        String userId = "111111";
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

        OrderRequestModel orderRequestModel = OrderRequestModel.builder()
                .items(items)
                .message("message1")
                .build();

        OrderResponseModel orderResponseModel = OrderResponseModel.builder()
                .orderId("123")
                .user(userId)
                .items(items)
                .message("message1")
                .build();

        when(restTemplate.postForObject(ORDER_SERVICE_BASE_URL + "/" + userId + "/orders", orderRequestModel, OrderResponseModel.class))
                .thenReturn(orderResponseModel);

        OrderResponseModel actualOrderResponseModel = orderServiceClient.addOrder(orderRequestModel, "111111");
        assertArrayEquals(new OrderResponseModel[]{orderResponseModel}, new OrderResponseModel[]{actualOrderResponseModel});
    }

    @Test
    void addOrder_whenRestTemplateThrowsHttpClientErrorExceptionWithStatusNotFound_thenThrowNotFoundException() {
        String userId = "111111";
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

        OrderRequestModel orderRequestModel = OrderRequestModel.builder()
                .items(items)
                .message("message1")
                .build();

        when(restTemplate.postForObject(ORDER_SERVICE_BASE_URL + "/" + userId + "/orders", orderRequestModel, OrderResponseModel.class))
                .thenThrow(new HttpClientErrorException(NOT_FOUND));

        assertThrows(NotFoundException.class, () -> orderServiceClient.addOrder(orderRequestModel, "111111"));
    }

    @Test
    void addOrder_whenRestTemplateThrowsHttpClientErrorExceptionWithStatusUnprocessableEntity_thenThrowInvalidInputException() {
        String userId = "111111";
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

        OrderRequestModel orderRequestModel = OrderRequestModel.builder()
                .items(items)
                .message("message1")
                .build();

        when(restTemplate.postForObject(ORDER_SERVICE_BASE_URL + "/" + userId + "/orders", orderRequestModel, OrderResponseModel.class))
                .thenThrow(new HttpClientErrorException(NOT_FOUND));

        assertThrows(NotFoundException.class, () -> orderServiceClient.addOrder(orderRequestModel, "111111"));
    }

    @Test
    void updateOrder(){
        String userId = "111111";
        String orderId = "123";
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

        OrderRequestModel orderRequestModel = OrderRequestModel.builder()
                .items(items)
                .message("message1")
                .build();

        OrderResponseModel orderResponseModel = OrderResponseModel.builder()
                .orderId(orderId)
                .user(userId)
                .items(items)
                .message("message1")
                .build();
        HttpEntity<OrderRequestModel> orderRequestModelHttpEntity = new HttpEntity<>(orderRequestModel);

        when(restTemplate.exchange(ORDER_SERVICE_BASE_URL + "/orders/" + orderId, HttpMethod.PUT, orderRequestModelHttpEntity, OrderResponseModel.class))
                .thenReturn((new ResponseEntity<>(orderResponseModel, HttpStatus.OK)));

        OrderResponseModel actualOrderResponseModel = orderServiceClient.updateOrder(orderRequestModel, orderId);
        assertEquals(orderResponseModel, actualOrderResponseModel);
    }

    @Test
    void updateOrder_whenRestTemplateThrowsHttpClientErrorExceptionWithStatusNotFound_thenThrowNotFoundException() {
        String userId = "111111";
        String orderId = "123";
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

        OrderRequestModel orderRequestModel = OrderRequestModel.builder()
                .items(items)
                .message("message1")
                .build();

        HttpEntity<OrderRequestModel> orderRequestModelHttpEntity = new HttpEntity<>(orderRequestModel);

        when(restTemplate.exchange(ORDER_SERVICE_BASE_URL + "/orders/" + orderId, HttpMethod.PUT, orderRequestModelHttpEntity, OrderResponseModel.class))
                .thenThrow(new HttpClientErrorException(NOT_FOUND));

        assertThrows(NotFoundException.class, () -> orderServiceClient.updateOrder(orderRequestModel, orderId));
    }

    @Test
    void updateOrder_whenRestTemplateThrowsHttpClientErrorExceptionWithStatusUnprocessableEntity_thenThrowInvalidInputException() {
        String userId = "111111";
        String orderId = "123";
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

        OrderRequestModel orderRequestModel = OrderRequestModel.builder()
                .items(items)
                .message("message1")
                .build();

        HttpEntity<OrderRequestModel> orderRequestModelHttpEntity = new HttpEntity<>(orderRequestModel);

        when(restTemplate.exchange(ORDER_SERVICE_BASE_URL + "/orders/" + orderId, HttpMethod.PUT, orderRequestModelHttpEntity, OrderResponseModel.class))
                .thenThrow(new HttpClientErrorException(NOT_FOUND));

        assertThrows(NotFoundException.class, () -> orderServiceClient.updateOrder(orderRequestModel, orderId));
    }


}