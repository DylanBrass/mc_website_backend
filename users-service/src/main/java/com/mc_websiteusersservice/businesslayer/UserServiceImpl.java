package com.mc_websiteusersservice.businesslayer;

import com.mc_websiteusersservice.datalayer.User;
import com.mc_websiteusersservice.datalayer.UserRepository;
import com.mc_websiteusersservice.datalayer.ResetPasswordToken;
import com.mc_websiteusersservice.datalayer.ResetPasswordTokenRepository;
import com.mc_websiteusersservice.datamapperlayer.UserRequestMapper;
import com.mc_websiteusersservice.datamapperlayer.UserResponseMapper;
import com.mc_websiteusersservice.presentationlayer.UserRequestModel;
import com.mc_websiteusersservice.presentationlayer.UserResponseModel;
import com.mc_websiteusersservice.utils.exceptions.ExistingUserNotFoundException;
import com.mc_websiteusersservice.utils.exceptions.UserAlreadyExistsException;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserResponseMapper userResponseMapper;
    private final UserRequestMapper userRequestMapper;
    private final ResetPasswordTokenRepository tokenRepository;
    public UserServiceImpl(UserRepository userRepository, UserResponseMapper userResponseMapper, UserRequestMapper userRequestMapper, ResetPasswordTokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.userResponseMapper = userResponseMapper;
        this.userRequestMapper = userRequestMapper;
        this.tokenRepository = tokenRepository;
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
            userWithPassword.setPassword(sb.toString());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }

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
    public void updateResetPasswordToken(String token, String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            if(tokenRepository.findResetPasswordTokenByUserIdentifier_UserId(user.getUserIdentifier().getUserId()) != null){
                tokenRepository.delete(tokenRepository.findResetPasswordTokenByUserIdentifier_UserId(user.getUserIdentifier().getUserId()));
            }
            //Hash the tokens
            ResetPasswordToken resetPasswordToken = new ResetPasswordToken(user.getUserIdentifier().getUserId(),token);
            tokenRepository.save(resetPasswordToken);
        } else {
        }
    }

    @Override
    public UserResponseModel getByResetPasswordToken(String token) {
        ResetPasswordToken resetPasswordToken = tokenRepository.findResetPasswordTokenByToken(token);
        final Calendar cal = Calendar.getInstance();

        if(resetPasswordToken.getExpiryDate().after(cal.getTime()))
            return userResponseMapper.entityToResponseModel(userRepository.findUserByUserIdentifier_UserId(resetPasswordToken.getUserIdentifier().getUserId()));
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
            User user = userRepository.findUserByUserIdentifier_UserId(resetPasswordToken.getUserIdentifier().getUserId());
            user.setPassword(encodedPassword);

            userRepository.save(user);
            tokenRepository.delete(resetPasswordToken);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }


}
