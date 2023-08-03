package com.mc_website.customersservice.businesslayer;

import com.mc_website.customersservice.datalayer.Customer;
import com.mc_website.customersservice.datalayer.CustomerRepository;
import com.mc_website.customersservice.datalayer.ResetPasswordToken;
import com.mc_website.customersservice.datalayer.ResetPasswordTokenRepository;
import com.mc_website.customersservice.datamapperlayer.CustomerRequestMapper;
import com.mc_website.customersservice.datamapperlayer.CustomerResponseMapper;
import com.mc_website.customersservice.presentationlayer.CustomerRequestModel;
import com.mc_website.customersservice.presentationlayer.CustomerResponseModel;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.time.chrono.ChronoLocalDate;
import java.time.temporal.TemporalAccessor;
import java.util.Calendar;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerResponseMapper customerResponseMapper;
    private final CustomerRequestMapper customerRequestMapper;
    private final ResetPasswordTokenRepository tokenRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository, CustomerResponseMapper customerResponseMapper, CustomerRequestMapper customerRequestMapper, ResetPasswordTokenRepository tokenRepository) {
        this.customerRepository = customerRepository;
        this.customerResponseMapper = customerResponseMapper;
        this.customerRequestMapper = customerRequestMapper;
        this.tokenRepository = tokenRepository;
    }
    @Override
    public List<CustomerResponseModel> getCustomers() {
        return customerResponseMapper.entityListToResponseModelList(customerRepository.findAll());
    }

    @Override
    public CustomerResponseModel getCustomerById(String customerId) {
        if(!customerRepository.existsByCustomerIdentifier_CustomerId(customerId))
            throw new RuntimeException("Customer with id: " + customerId + " does not exist");
        return customerResponseMapper.entityToResponseModel(customerRepository.findByCustomerIdentifier_CustomerId(customerId));
    }

    @Override
    public CustomerResponseModel addCustomer(CustomerRequestModel customerRequestModel) {

        Customer customerWithPassword = customerRequestMapper.requestModelToEntity(customerRequestModel);
        try
        {
            // Create MessageDigest instance for MD5
            MessageDigest md = MessageDigest.getInstance("MD5");

            // Add password bytes to digest
            md.update(customerRequestModel.getPassword().getBytes());

            // Get the hash's bytes
            byte[] bytes = md.digest();

            // This bytes[] has bytes in decimal format. Convert it to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }

            // Get complete hashed password in hex format
            customerWithPassword.setPassword(sb.toString());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }

        Customer customer = customerRepository.save(customerWithPassword);

        return customerResponseMapper.entityToResponseModel(customer);
    }

    @Override
    public CustomerResponseModel updateCustomer(String customerId, CustomerRequestModel customerRequestModel) {
        Customer customer = customerRequestMapper.requestModelToEntity(customerRequestModel);
        Customer existingCustomer = customerRepository.findByCustomerIdentifier_CustomerId(customerId);
        if(!customerRepository.existsByCustomerIdentifier_CustomerId(customerId))
            throw new RuntimeException("Customer with id: " + customerId + " does not exist");
        customer.setId(existingCustomer.getId());
        customer.setCustomerIdentifier(existingCustomer.getCustomerIdentifier());
        customer.setPassword(existingCustomer.getPassword());
        Customer updatedCustomer = customerRepository.save(customer);

        return customerResponseMapper.entityToResponseModel(updatedCustomer);
    }

    @Override
    public CustomerResponseModel getCustomerByEmail(String email) {
        if(customerRepository.findByEmail(email) == null)
            throw new RuntimeException("Customer with email: " + email + " does not exist");
        return customerResponseMapper.entityToResponseModel(customerRepository.findByEmail(email));
    }

    @Override
    public CustomerResponseModel getCustomerByEmailAndPassword(String email, String password) {
        Customer customerWithPassword = customerRepository.findByEmail(email);
        if(customerWithPassword == null)
            throw new RuntimeException("Customer with email: " + email + " does not exist");
        // Encrypt the password here
        try
        {
            // Create MessageDigest instance for MD5
            MessageDigest md = MessageDigest.getInstance("MD5");

            // Add password bytes to digest
            md.update(password.getBytes());

            // Get the hash's bytes
            byte[] bytes = md.digest();

            // This bytes[] has bytes in decimal format. Convert it to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }

            // Get complete hashed password in hex format
            customerWithPassword.setPassword(sb.toString());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }

        // Check if the customer exists
        if(customerRepository.findByEmailAndPassword(email, customerWithPassword.getPassword()) == null)
            throw new RuntimeException("Customer with email: " + email + " and password: " + customerWithPassword.getPassword() + " does not exist");
        return customerResponseMapper.entityToResponseModel(customerRepository.findByEmailAndPassword(email, customerWithPassword.getPassword()));
    }


    @Override
    public void deleteCustomer(String customerId) {
        if(!customerRepository.existsByCustomerIdentifier_CustomerId(customerId))
            throw new RuntimeException("Customer with id: " + customerId + " does not exist");
        customerRepository.delete(customerRepository.findByCustomerIdentifier_CustomerId(customerId));
    }

    @Override
    public void updateResetPasswordToken(String token, String email) {
        Customer customer = customerRepository.findByEmail(email);
        if (customer != null) {
            if(tokenRepository.findResetPasswordTokenByCustomerIdentifier_CustomerId(customer.getCustomerIdentifier().getCustomerId()) != null){
                tokenRepository.delete(tokenRepository.findResetPasswordTokenByCustomerIdentifier_CustomerId(customer.getCustomerIdentifier().getCustomerId()));
            }
            //Hash the tokens
            ResetPasswordToken resetPasswordToken = new ResetPasswordToken(customer.getCustomerIdentifier().getCustomerId(),token);
            tokenRepository.save(resetPasswordToken);
        } else {
        }
    }

    @Override
    public CustomerResponseModel getByResetPasswordToken(String token) {
        ResetPasswordToken resetPasswordToken = tokenRepository.findResetPasswordTokenByToken(token);
        final Calendar cal = Calendar.getInstance();

        if(resetPasswordToken.getExpiryDate().after(cal.getTime()))
            return customerResponseMapper.entityToResponseModel(customerRepository.findByCustomerIdentifier_CustomerId(resetPasswordToken.getCustomerIdentifier().getCustomerId()));
        else
            throw new IllegalArgumentException("Token is expired (in getByResetPasswordToken()");    }


    @Override
    public void updatePassword(String newPassword, String token) {
        try
        {
            final Calendar cal = Calendar.getInstance();

            ResetPasswordToken resetPasswordToken = tokenRepository.findResetPasswordTokenByToken(token);
            if(resetPasswordToken.getExpiryDate().before(cal.getTime())){
                throw new IllegalArgumentException("Token expired");
            }
            MessageDigest md = MessageDigest.getInstance("MD5");

            md.update(newPassword.getBytes());

            byte[] bytes = md.digest();

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }

            String encodedPassword = sb.toString();
            Customer customer = customerRepository.findByCustomerIdentifier_CustomerId(resetPasswordToken.getCustomerIdentifier().getCustomerId());
            customer.setPassword(encodedPassword);

            customerRepository.save(customer);
            tokenRepository.delete(resetPasswordToken);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }


}
