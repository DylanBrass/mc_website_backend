package com.mc_website.ordersservice.datalayer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.ArrayList;
import java.util.List;

import static com.mc_website.ordersservice.datalayer.ItemType.MTG;
import static com.mc_website.ordersservice.datalayer.ItemType.RPG;
import static com.mc_website.ordersservice.datalayer.OrderType.ORDER;

import static com.mc_website.ordersservice.datalayer.OrderType.PRE_ORDER;
import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@AutoConfigureDataMongo
class OrdersPersistenceTest {

    private Orders preSavedOrder;

    @Autowired
    private OrdersRepository ordersRepository;

    @BeforeEach
    public void setup() {
        ordersRepository.deleteAll();
        UserIdentifier user = new UserIdentifier("123");

        // Create order
        List<Item> items1 = new ArrayList<>();
        Item item1 = new Item("Item 1", MTG, "Desc 1", ORDER, 2);
        Item item2 = new Item("Item 2", RPG, "Desc 2", ORDER, 3);
        items1.add(item1);
        items1.add(item2);
        String message1 = "Message of order 1";

        List<Item> items2 = new ArrayList<>();
        Item item3 = new Item("Item 1", MTG, "Desc 1", ORDER, 2);
        Item item4 = new Item("Item 2", RPG, "Desc 2", ORDER, 3);
        items2.add(item3);
        items2.add(item4);
        String message2 = "Message of order 2";

        preSavedOrder = ordersRepository.save(new Orders(user, items1, message1));
        ordersRepository.save(new Orders(user, items2, message2));
    }

    @Test
    public void findByOrderIdentifier_OrderId_ShouldSucceed() {
        //act
        Orders orderFound = ordersRepository.getOrdersByOrderIdentifier_OrderId(preSavedOrder.getOrderIdentifier().getOrderId());

        //assert
        assertNotNull(orderFound);
        assertEquals(preSavedOrder.getUser().getUserId(), orderFound.getUser().getUserId());
        assertEquals(preSavedOrder.getItems(), orderFound.getItems());
        assertEquals(preSavedOrder.getMessage(), orderFound.getMessage());
        assertEquals(preSavedOrder.getOrderIdentifier().getOrderId(), orderFound.getOrderIdentifier().getOrderId());
    }

    @Test
    public void findByInvalidOrderIdentifier_OrderId_ShouldReturnNull() {
        //act
        Orders orderFound = ordersRepository.getOrdersByOrderIdentifier_OrderId(preSavedOrder.getOrderIdentifier().getOrderId() + 1);

        //assert
        assertNull(orderFound);
    }

    @Test
    public void existsOrderIdentifier_OrderId_ShouldReturnTrue() {
        // act
        Boolean orderFound = ordersRepository.existsByOrderIdentifier_OrderId(preSavedOrder.getOrderIdentifier().getOrderId());
        // assert
        assertTrue(orderFound);
    }

    @Test
    public void existsInvalidOrderIdentifier_OrderId_ShouldReturnFalse() {
        // act
        Boolean orderFound = ordersRepository.existsByOrderIdentifier_OrderId(preSavedOrder.getOrderIdentifier().getOrderId() + 1);
        // assert
        assertFalse(orderFound);
    }

    @Test
    public void getOrdersByUser_UserId_ShouldSucceed() {
        //act
        List<Orders> ordersFound = ordersRepository.getOrdersByUser_UserId(preSavedOrder.getUser().getUserId());

        //assert
        assertNotNull(ordersFound);
        assertEquals(2, ordersFound.size());
        assertEquals(preSavedOrder.getUser().getUserId(), ordersFound.get(0).getUser().getUserId());
        assertEquals(preSavedOrder.getItems(), ordersFound.get(0).getItems());
        assertEquals(preSavedOrder.getMessage(), ordersFound.get(0).getMessage());
        assertEquals(preSavedOrder.getOrderIdentifier().getOrderId(), ordersFound.get(0).getOrderIdentifier().getOrderId());
    }

    @Test
    public void getOrdersByInvalidUser_UserId_ShouldReturnNull() {
        //act
        List<Orders> ordersFound = ordersRepository.getOrdersByUser_UserId(preSavedOrder.getUser().getUserId() + 1);

        //assert
        assertEquals(ordersFound, new ArrayList<>());
    }

    @Test
    public void saveNewOrder(){
        // Create order
        UserIdentifier user = new UserIdentifier("123");
        List<Item> items = new ArrayList<>();
        Item item1 = new Item("Item 1345", MTG, "Description 1", ORDER, 2);
        Item item2 = new Item("Item 2567", RPG, "Description 2", PRE_ORDER, 3);
        items.add(item1);
        items.add(item2);
        String message = "Message of order 1";
        Orders order = new Orders(user, items, message);

        Orders savedOrder = ordersRepository.save(order);
        assertNotNull(savedOrder);
        assertEquals(user.getUserId(), savedOrder.getUser().getUserId());
        assertEquals(items.stream().count(), savedOrder.getItems().stream().count());
        assertEquals("Item 1345", savedOrder.getItems().get(0).item);
        assertEquals("Item 2567", savedOrder.getItems().get(1).item);
        assertEquals("Description 1", savedOrder.getItems().get(0).description);
        assertEquals("Description 2", savedOrder.getItems().get(1).description);
        assertEquals(MTG, savedOrder.getItems().get(0).itemType);
        assertEquals(RPG, savedOrder.getItems().get(1).itemType);
        assertEquals(ORDER, savedOrder.getItems().get(0).orderType);
        assertEquals(PRE_ORDER, savedOrder.getItems().get(1).orderType);
        assertEquals(message, savedOrder.getMessage());
        assertEquals(order.getOrderIdentifier().getOrderId(), savedOrder.getOrderIdentifier().getOrderId());
    }

    @Test
    public void updateOrder(){
        // Update order
        preSavedOrder.getItems().get(0).setItem("Item 1345 updated");
        preSavedOrder.getItems().get(0).setDescription("Description 1 updated");
        preSavedOrder.getItems().get(0).setItemType(RPG);
        preSavedOrder.getItems().get(0).setOrderType(PRE_ORDER);
        preSavedOrder.getItems().get(0).setQuantity(4);
        preSavedOrder.getItems().get(1).setItem("Item 2567 updated");
        preSavedOrder.getItems().get(1).setDescription("Description 2 updated");
        preSavedOrder.getItems().get(1).setItemType(MTG);
        preSavedOrder.getItems().get(1).setOrderType(ORDER);
        preSavedOrder.getItems().get(1).setQuantity(5);
        preSavedOrder.setMessage("Message of order 1 updated");

        UserIdentifier user = new UserIdentifier("12344");
        preSavedOrder.setUser(user);

        Orders updatedOrder = ordersRepository.save(preSavedOrder);
        assertNotNull(updatedOrder);

        assertEquals(2, updatedOrder.getItems().stream().count());
        assertEquals("Item 1345 updated", updatedOrder.getItems().get(0).item);
        assertEquals("Item 2567 updated", updatedOrder.getItems().get(1).item);
        assertEquals("Description 1 updated", updatedOrder.getItems().get(0).description);
        assertEquals("Description 2 updated", updatedOrder.getItems().get(1).description);
        assertEquals(RPG, updatedOrder.getItems().get(0).itemType);
        assertEquals(MTG, updatedOrder.getItems().get(1).itemType);
        assertEquals(PRE_ORDER, updatedOrder.getItems().get(0).orderType);
        assertEquals(ORDER, updatedOrder.getItems().get(1).orderType);
        assertEquals(4, updatedOrder.getItems().get(0).quantity);
        assertEquals(5, updatedOrder.getItems().get(1).quantity);
        assertEquals("Message of order 1 updated", updatedOrder.getMessage());
        assertEquals(preSavedOrder.getOrderIdentifier().getOrderId(), updatedOrder.getOrderIdentifier().getOrderId());
    }

    @Test
    public void deleteOrder(){
        // Delete order
        ordersRepository.delete(preSavedOrder);
        assertFalse(ordersRepository.existsByOrderIdentifier_OrderId(preSavedOrder.getOrderIdentifier().getOrderId()));
    }


}
