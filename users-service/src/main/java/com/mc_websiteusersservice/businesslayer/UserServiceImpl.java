package com.mc_websiteusersservice.businesslayer;

import com.mc_websiteusersservice.datalayer.ResetPasswordToken;
import com.mc_websiteusersservice.datalayer.ResetPasswordTokenRepository;
import com.mc_websiteusersservice.datalayer.User;
import com.mc_websiteusersservice.datalayer.UserRepository;
import com.mc_websiteusersservice.datamapperlayer.UserRequestMapper;
import com.mc_websiteusersservice.datamapperlayer.UserResponseMapper;
import com.mc_websiteusersservice.presentationlayer.UserRequestModel;
import com.mc_websiteusersservice.presentationlayer.UserResetPwdRequestModel;
import com.mc_websiteusersservice.presentationlayer.UserResetPwdWithTokenRequestModel;
import com.mc_websiteusersservice.presentationlayer.UserResponseModel;
import com.mc_websiteusersservice.utils.exceptions.ExistingUserNotFoundException;
import com.mc_websiteusersservice.utils.exceptions.UserAlreadyExistsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.*;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserResponseMapper userResponseMapper;
    private final UserRequestMapper userRequestMapper;
    private final ResetPasswordTokenRepository tokenRepository;
    ResetPasswordTokenRepository resetPasswordTokenRepository;
    private final String username;

    private String salt = BCrypt.gensalt(10);
    private final String password;
    Session session;

    public UserServiceImpl(UserRepository userRepository, UserResponseMapper userResponseMapper, UserRequestMapper userRequestMapper, ResetPasswordTokenRepository tokenRepository, @Value("${spring.mail.username}") String username, @Value("${spring.mail.password}") String password, ResetPasswordTokenRepository resetPasswordTokenRepository) {
        this.userRepository = userRepository;
        this.userResponseMapper = userResponseMapper;
        this.userRequestMapper = userRequestMapper;
        this.tokenRepository = tokenRepository;
        this.username = username;
        this.password = password;
        this.resetPasswordTokenRepository = resetPasswordTokenRepository;
        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true"); //TLS

        session = javax.mail.Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                        return new javax.mail.PasswordAuthentication(username, password);
                    }
                });
    }

    @Override
    public List<UserResponseModel> getUsers() {
        return userResponseMapper.entityListToResponseModelList(userRepository.findAll());
    }

    @Override
    public UserResponseModel getUserById(String userId) {
        // Check if the user exists
        if(!userRepository.existsByUserIdentifier_UserId(userId))
            throw new ExistingUserNotFoundException("User with id " + userId + " not found.");

        return userResponseMapper.entityToResponseModel(userRepository.findUserByUserIdentifier_UserId(userId));
    }

    @Override
    public UserResponseModel addUser(UserRequestModel userRequestModel) {
        // Check if the user exists
        if(userRepository.existsByEmail(userRequestModel.getEmail()))
            throw new UserAlreadyExistsException("User with email " + userRequestModel.getEmail() + " already exists.");
        User userWithPassword = userRequestMapper.requestModelToEntity(userRequestModel);
      userWithPassword.setPassword(userRequestModel.getPassword());

        User user = userRepository.save(userWithPassword);

        return userResponseMapper.entityToResponseModel(user);
    }

    @Override
    public UserResponseModel updateUser(String userId, UserRequestModel userRequestModel) {
        User user = userRequestMapper.requestModelToEntity(userRequestModel);
        User existingUser = userRepository.findUserByUserIdentifier_UserId(userId);
        // Check if the user exists
        if(!userRepository.existsByUserIdentifier_UserId(userId))
            throw new ExistingUserNotFoundException("User with id " + userId + " not found.");
        user.setId(existingUser.getId());
        user.setUserIdentifier(existingUser.getUserIdentifier());
        user.setPassword(existingUser.getPassword());
        User updatedUser = userRepository.save(user);

        return userResponseMapper.entityToResponseModel(updatedUser);
    }

    @Override
    public UserResponseModel getUserByEmail(String email) {
        // Check if the user exists
        if(userRepository.findByEmail(email) == null)
            throw new ExistingUserNotFoundException("User with email " + email + " does not exist.");
        return userResponseMapper.entityToResponseModel(userRepository.findByEmail(email));
    }

    @Override
    public UserResponseModel getUserByEmailAndPassword(String email, String password) {
        User userWithPassword = userRepository.findByEmail(email);
        if(userWithPassword == null)
            throw new ExistingUserNotFoundException("User with email " + email + " does not exist.");

        String encodedPassword = BCrypt.hashpw(password, BCrypt.gensalt(10));

            // Get complete hashed password in hex format
            userWithPassword.setPassword(encodedPassword);


        // Check if the user exists
        if(userRepository.findByEmailAndPassword(email, userWithPassword.getPassword()) == null)
            throw new ExistingUserNotFoundException("Email or password is incorrect.");
        return userResponseMapper.entityToResponseModel(userRepository.findByEmailAndPassword(email, userWithPassword.getPassword()));
    }


    @Override
    public void deleteUser(String userId) {
        if(!userRepository.existsByUserIdentifier_UserId(userId))
            throw new ExistingUserNotFoundException("User with id " + userId + " not found.");
        userRepository.delete(userRepository.findUserByUserIdentifier_UserId(userId));
    }

    @Override
    public Model processForgotPassword(UserResetPwdRequestModel userResetPwdRequestModel, Model model) {
        String email = userResetPwdRequestModel.getEmail();
        String token = UUID.randomUUID().toString();
        log.info("Generated token: " + token);
        try {
            getUserByEmail(email);
        }
        catch(RuntimeException e){
            model.addAttribute("message", "This Email is not registered to any account !");
            return model;
        }

        try {

            updateResetPasswordToken( token, email);
            log.info("Line 155");


            String resetPasswordLink =  userResetPwdRequestModel.getUrl()+ "/api/v1/users/reset_password?token=" + token;
            sendEmail(email, resetPasswordLink);
            model.addAttribute("message", "We have sent a reset password link to your email. Please check.");
        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
        }
    return model;
    }

    @Override
    public void updateResetPasswordToken(String token, String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            if(tokenRepository.findResetPasswordTokenByUserIdentifier_UserId(user.getUserIdentifier().getUserId()) != null){
                tokenRepository.delete(tokenRepository.findResetPasswordTokenByUserIdentifier_UserId(user.getUserIdentifier().getUserId()));
            }
            //Hash the tokens
            ResetPasswordToken resetPasswordToken = new ResetPasswordToken(user.getUserIdentifier().getUserId(),BCrypt.hashpw(token,salt));
            tokenRepository.save(resetPasswordToken);
        } else {
            throw new IllegalArgumentException("Could not find any customer with the email " + email);
        }
    }

    @Override
    public UserResponseModel getByResetPasswordToken(String token) {
        log.info("Token: " + token);
        log.info("Line 187");
        String hashedToken = BCrypt.hashpw(token, salt);
        log.info("Hashed token: " + hashedToken);
        ResetPasswordToken resetPasswordToken = tokenRepository.findResetPasswordTokenByToken(hashedToken);
        final Calendar cal = Calendar.getInstance();

        if(resetPasswordToken.getExpiryDate().after(cal.getTime()))
            return userResponseMapper.entityToResponseModel(userRepository.findUserByUserIdentifier_UserId(resetPasswordToken.getUserIdentifier().getUserId()));
        else
            throw new IllegalArgumentException("Token is expired (in getByResetPasswordToken()");    }


    @Override
    public void updatePassword(String newPassword, String token) {

            final Calendar cal = Calendar.getInstance();
            log.info("line 201");
            ResetPasswordToken resetPasswordToken = tokenRepository.findResetPasswordTokenByToken(BCrypt.hashpw(token, salt));
            if(resetPasswordToken.getExpiryDate().before(cal.getTime())){
                throw new IllegalArgumentException("Token expired");
            }

            String encodedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt(10));
            User user = userRepository.findUserByUserIdentifier_UserId(resetPasswordToken.getUserIdentifier().getUserId());
            user.setPassword(encodedPassword);

            userRepository.save(user);
            tokenRepository.delete(resetPasswordToken);


    }

    @Override
    public Model showResetPasswordForm(Map<String, String> querryParams, Model model) {
        String token = querryParams.get("token");


        UserResponseModel userResponseModel = getByResetPasswordToken(token);
        model.addAttribute("token", token);

        if (userResponseModel == null) {
            model.addAttribute("message", "Invalid Token");
        }
        else{
            model.addAttribute("message", "You have successfully changed your password.");
        }
        return model;
    }

    @Override
    public Model processResetPassword(UserResetPwdWithTokenRequestModel resetRequest, Model model) {
        String token = resetRequest.getToken();
        String password = resetRequest.getPassword();



        //Hash token
        UserResponseModel userResponseModel = getByResetPasswordToken(token);



        model.addAttribute("title", "Reset your password");

        if (userResponseModel == null) {
            model.addAttribute("message", "Invalid Token");
        } else {
            updatePassword(password, token);

            model.addAttribute("message", "You have successfully changed your password.");
        }
        return model;
    }


    public void sendEmail(String recipientEmail, String link) throws MessagingException, UnsupportedEncodingException, InterruptedException {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(username));
        message.setRecipients(
                Message.RecipientType.TO,
                InternetAddress.parse(recipientEmail) //grif2004@hotmail.com || kehayova.mila@gmail.com
        );
        message.setSubject("Change Password");

        message.setText("Test : " + link);

        Transport.send(message);
    }


}
