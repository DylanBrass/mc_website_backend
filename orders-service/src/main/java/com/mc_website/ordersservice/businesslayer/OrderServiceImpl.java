package com.mc_website.ordersservice.businesslayer;

import com.mc_website.ordersservice.datalayer.*;
import com.mc_website.ordersservice.datamapperlayer.OrderRequestMapper;
import com.mc_website.ordersservice.datamapperlayer.OrderResponseMapper;
import com.mc_website.ordersservice.domainclientlayer.UserServiceClient;
import com.mc_website.ordersservice.presentationlayer.Customer.UserResponseModel;
import com.mc_website.ordersservice.presentationlayer.OrderRequestModel;
import com.mc_website.ordersservice.presentationlayer.OrderResponseModel;
import com.mc_website.ordersservice.utils.exceptions.InvalidInputException;
import com.mc_website.ordersservice.utils.exceptions.NotFoundException;
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

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {
    private final String storeEmail = "grif2004@hotmail.com";
    private final String username;
    private final String password;
    private final OrderRequestMapper orderRequestMapper;
    private final OrderResponseMapper orderResponseMapper;
    private final OrdersRepository ordersRepository;

    private final UserServiceClient userServiceClient;
    Session session;
    public OrderServiceImpl(@Value("${spring.mail.username}") String username,@Value("${spring.mail.password}") String password, OrderRequestMapper orderRequestMapper, OrderResponseMapper orderResponseMapper, OrdersRepository ordersRepository, UserServiceClient userServiceClient) {
        this.username = username;
        this.password = password;
        this.orderRequestMapper = orderRequestMapper;
        this.orderResponseMapper = orderResponseMapper;
        this.ordersRepository = ordersRepository;
        this.userServiceClient = userServiceClient;
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
        savedOrder.setCustomer(new UserIdentifier(customerId));
        savedOrder.setOrderIdentifier(new OrderIdentifier());
        List<Item> items = new ArrayList<>(orderRequestModel.getItems());
        savedOrder.setItems(items);

        UserResponseModel userResponseModel = userServiceClient.getCustomer(customerId);
        savedOrder.setCustomer(new UserIdentifier(userResponseModel.getCustomerId()));

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(storeEmail) //grif2004@hotmail.com || kehayova.mila@gmail.com || denisanhategan@gmail.com
            );
            message.setSubject("New order : " + savedOrder.getOrderIdentifier().getOrderId());
            String messageStr = "<b>Customer </b>: "+ userResponseModel.getFirstName() + userResponseModel.getLastName() +"<br><b>Message : </b></br>"+savedOrder.getMessage() + "<br></br>";
            for(int i=0; i< items.size();i++) {
                messageStr +=
                        ("<br><table border ='6'> <tr> <td><b>Items : </b></br>"+ savedOrder.getItems().get(i).getItem() +
                                "<br><b>Item Type : </b></br>" +savedOrder.getItems().get(i).getItemType().toString()+ "<br><b>Order Type : </b></br>" + savedOrder.getItems().get(i).getOrderType() +
                                "<br><b>Quantity : </b></br>" + savedOrder.getItems().get(i).getQuantity() +"<br><b>Description: </b></br>" + savedOrder.getItems().get(i).getDescription() + "</td></tr></table><br>") ;
            }
            message.setContent("" + messageStr + "","text/html");
            Orders order = ordersRepository.insert(savedOrder);

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
        return orderResponseMapper.entityToResponseModel(ordersRepository.getOrdersByOrderIdentifier_OrderId(orderId));
    }

    @Override
    public OrderResponseModel updateOrder(OrderRequestModel orderRequestModel, String orderId ) throws MessagingException {
        Orders existingOrder = ordersRepository.getOrdersByOrderIdentifier_OrderId(orderId);
        Orders order=orderRequestMapper.requestModelToEntity(orderRequestModel);
        order.setCustomer(new UserIdentifier(existingOrder.getCustomer().getCustomerId()));
        order.setId(existingOrder.getId());
        order.setOrderIdentifier(existingOrder.getOrderIdentifier());
        UserResponseModel userResponseModel = userServiceClient.getCustomer(existingOrder.getCustomer().getCustomerId());

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(storeEmail) //grif2004@hotmail.com || kehayova.mila@gmail.com || denisanhategan@gmail.com
            );
            message.setSubject("Updated order : " + order.getOrderIdentifier().getOrderId());
            String messageStr = "Customer : "+ userResponseModel.getFirstName() + userResponseModel.getLastName()+ "Message : "+order.getMessage() + "\n";
            for(int i=0; i< order.getItems().size();i++) {
                messageStr +=
                        ("\nItems : "+ order.getItems().get(i).getItem() +
                                "\nItem Type : " +order.getItems().get(i).getItemType().toString()+ "\nOrder Type :" + order.getItems().get(i).getOrderType() +
                                "\nQuantity :" + order.getItems().get(i).getQuantity() +"\nDescription: " + order.getItems().get(i).getDescription());
            }
            message.setText(messageStr);
            Orders updatedOrder=ordersRepository.save(order);

            Transport.send(message);

            return orderResponseMapper.entityToResponseModel(updatedOrder);

        } catch (MessagingException e) {
            e.printStackTrace();
            throw new MessagingException(e.getMessage());
        }
    }

    @Override
    public void deleteOrder(String orderId) {
        Orders order = ordersRepository.getOrdersByOrderIdentifier_OrderId(orderId);
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
