package com.mc_website.ordersservice.datalayer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;

import static com.mc_website.ordersservice.datalayer.ItemType.MTG;
import static com.mc_website.ordersservice.datalayer.ItemType.RPG;
import static com.mc_website.ordersservice.datalayer.OrderType.ORDER;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@AutoConfigureDataMongo
class OrdersPersistenceTest {
    private Orders preSavedOrders;

    @Autowired
    private OrdersRepository ordersRepository;

    @BeforeEach
    public void setup() {
        ordersRepository.deleteAll();
        UserIdentifier user = new UserIdentifier("123");
        List<Item> items = new ArrayList<>();
        Item item1 = new Item("Item 1", MTG, "Desc 1", ORDER, 2);
        Item item2 = new Item("Item 2", RPG, "Desc 2", ORDER, 3);
        items.add(item1);
        items.add(item2);
        String message = "Message of order";
        preSavedOrders = ordersRepository.save(new Orders(user, items, message));
    }

    @Test
    public void findByOrderIdentifier_OrderId_ShouldSucceed() {
        //act
        Orders ordersFound = ordersRepository.getOrdersByOrderIdentifier_OrderId(preSavedOrders.getOrderIdentifier().getOrderId());

        //assert
        assertNotNull(ordersFound);
        assertThat(preSavedOrders, samePropertyValuesAs(ordersFound));
    }

    @Test
    public void findByInvalidOrderIdentifier_OrderId_ShouldReturnNull() {
        //act
        Orders ordersFound = ordersRepository.getOrdersByOrderIdentifier_OrderId(preSavedOrders.getOrderIdentifier().getOrderId() + 1);

        //assert
        assertNull(ordersFound);
    }

    @Test
    public void existsByOrderIdentifier_OrderId_ShouldReturnTrue() {
        // act
        Boolean ordersFound = ordersRepository.existsByOrderIdentifier_OrderId(preSavedOrders.getOrderIdentifier().getOrderId());
        // assert
        assertTrue(ordersFound);
    }

    @Test
    public void existsInvalidOrderIdentifier_OrderId_ShouldReturnFalse() {
        // act
        Boolean ordersFound = ordersRepository.existsByOrderIdentifier_OrderId(preSavedOrders.getOrderIdentifier().getOrderId() + 1);
        // assert
        assertFalse(ordersFound);
    }

    @Test
    public void getOrdersByUser_UserId_ShouldSucceed() {
        //act
        List<Orders> ordersFound = ordersRepository.getOrdersByUser_UserId(preSavedOrders.getUser().getUserId());

        //assert
        assertNotNull(ordersFound);
        assertThat(preSavedOrders, samePropertyValuesAs(ordersFound));
    }

    @Test
    public void getOrdersByInvalidUser_UserId_ShouldReturnNull() {
        //act
        List<Orders> ordersFound = ordersRepository.getOrdersByUser_UserId(preSavedOrders.getUser().getUserId() + 1);

        //assert
        assertNull(ordersFound);
    }

    @Test
    public void getOrdersByOrderIdentifier_OrderId_ShouldSucceed() {
        //act
        Orders ordersFound = ordersRepository.getOrdersByOrderIdentifier_OrderId(preSavedOrders.getOrderIdentifier().getOrderId());

        //assert
        assertNotNull(ordersFound);
        assertThat(preSavedOrders, samePropertyValuesAs(ordersFound));
    }

    @Test
    public void getOrdersByInvalidOrderIdentifier_OrderId_ShouldReturnNull() {
        //act
        Orders ordersFound = ordersRepository.getOrdersByOrderIdentifier_OrderId(preSavedOrders.getOrderIdentifier().getOrderId() + 1);

        //assert
        assertNull(ordersFound);
    }




}