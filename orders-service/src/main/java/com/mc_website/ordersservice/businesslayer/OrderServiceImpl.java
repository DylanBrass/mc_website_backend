package com.mc_website.ordersservice.businesslayer;

import com.mc_website.ordersservice.datalayer.*;
import com.mc_website.ordersservice.datamapperlayer.OrderRequestMapper;
import com.mc_website.ordersservice.datamapperlayer.OrderResponseMapper;
import com.mc_website.ordersservice.domainclientlayer.CustomerServiceClient;
import com.mc_website.ordersservice.presentationlayer.Customer.CustomerResponseModel;
import com.mc_website.ordersservice.presentationlayer.OrderRequestModel;
import com.mc_website.ordersservice.presentationlayer.OrderResponseModel;
import com.mc_website.ordersservice.utils.exceptions.InvalidInputException;
import com.mc_website.ordersservice.utils.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {
    private final String username;
    private final String password;
    private final OrderRequestMapper orderRequestMapper;
    private final OrderResponseMapper orderResponseMapper;
    private final OrdersRepository ordersRepository;

    private final CustomerServiceClient customerServiceClient;
    Session session;
    public OrderServiceImpl(@Value("${spring.mail.username}") String username,@Value("${spring.mail.password}") String password, OrderRequestMapper orderRequestMapper, OrderResponseMapper orderResponseMapper, OrdersRepository ordersRepository, CustomerServiceClient customerServiceClient) {
        this.username = username;
        this.password = password;
        this.orderRequestMapper = orderRequestMapper;
        this.orderResponseMapper = orderResponseMapper;
        this.ordersRepository = ordersRepository;
        this.customerServiceClient = customerServiceClient;
        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true"); //TLS

        session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
    }

    @Override
    public OrderResponseModel addOrder(OrderRequestModel orderRequestModel, String customerId) throws MessagingException {

        orderRequestModel.getItems().forEach(item -> {
            if (!findByOrderType(item.getOrderType().toString())) {
                throw new InvalidInputException("Order type entered is not a valid type : " + item.getOrderType().toString());
            }
            if (!findByItemType(item.getItemType().toString())) {
                throw new InvalidInputException("Item type entered is not a valid type : " + item.getItemType().toString());
            }
        });


        Orders savedOrder = orderRequestMapper.requestModelToEntity(orderRequestModel);
        savedOrder.setCustomer(new CustomerIdentifier(customerId));
        savedOrder.setOrderIdentifier(new OrderIdentifier());
        List<Item> items = new ArrayList<>(orderRequestModel.getItems());
        savedOrder.setItems(items);

        CustomerResponseModel customerResponseModel = customerServiceClient.getCustomer(customerId);
        savedOrder.setCustomer(new CustomerIdentifier(customerResponseModel.getCustomerId()));

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse("grif2004@hotmail.com") //grif2004@hotmail.com || kehayova.mila@gmail.com
            );
            message.setSubject("New order : " + savedOrder.getOrderIdentifier().getOrderId());
            message.setText("Customer : "+customerResponseModel.getFirstName() + " " + customerResponseModel.getLastName()+ "\nMessage : "+savedOrder.getMessage()+"\nItems : "+ savedOrder.getItems().toString());

            Orders order = ordersRepository.insert(savedOrder);
            if (order == null){
                throw new InvalidInputException("Order could not be saved !");
            }
            Transport.send(message);

            return orderResponseMapper.entityToResponseModel(order);

        } catch (MessagingException e) {
            e.printStackTrace();
            throw new MessagingException(e.getMessage());
        }
    }

    @Override
    public List<OrderResponseModel> getAllOrders() {
        return orderResponseMapper.entitiesResponseModel(ordersRepository.findAll());
    }

    @Override
    public List<OrderResponseModel> getAllOrdersForCustomer(String customerId) {

        return orderResponseMapper.entitiesResponseModel(ordersRepository.getOrdersByCustomer_CustomerId(customerId));
    }

    @Override
    public OrderResponseModel getOrderById(String orderId) {
        return orderResponseMapper.entityToResponseModel(ordersRepository.getOrOrderByOrderIdentifier_OrderId(orderId));
    }

    @Override
    public OrderResponseModel updateOrder(OrderRequestModel orderRequestModel, String orderId ) {
        Orders existingOrder = ordersRepository.getOrOrderByOrderIdentifier_OrderId(orderId);
        Orders order=orderRequestMapper.requestModelToEntity(orderRequestModel);
        order.setCustomer(new CustomerIdentifier(existingOrder.getCustomer().getCustomerId()));
        order.setId(existingOrder.getId());
        order.setOrderIdentifier(existingOrder.getOrderIdentifier());
        Orders updatedOrders=ordersRepository.insert(order);
        return orderResponseMapper.entityToResponseModel(updatedOrders);    }

    @Override
    public void deleteOrder(String orderId) {
        Orders order = ordersRepository.getOrOrderByOrderIdentifier_OrderId(orderId);
        if (order==null){
            throw new NotFoundException("No order was found with ID : " + orderId);
        }
        ordersRepository.delete(order);
    }

    public static Boolean findByOrderType(String typeStr) {
        boolean found = false;
        for (OrderType type : OrderType.values()) {
            if (type.name().equalsIgnoreCase(typeStr)) {
                found = true;
                break;
            }
        }
        return found;
    }
    public static Boolean findByItemType(String typeStr) {
        boolean found = false;
        for (ItemType type : ItemType.values()) {
            if (type.name().equalsIgnoreCase(typeStr)) {
                found = true;
                break;
            }
        }
        return found;
    }
}
