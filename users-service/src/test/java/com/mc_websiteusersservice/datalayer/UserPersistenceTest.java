package com.mc_websiteusersservice.datalayer;

import com.mc_websiteusersservice.presentationlayer.UserRequestModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserPersistenceTest {
    private User preSavedUser;

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    public void setup() {
        userRepository.deleteAll();
        preSavedUser = userRepository.save(new User("John", "Doe", "john.doe@example.com", "5147890876", "passWord!"));
    }

    @Test
    public void findByUserIdentifier_UserId_ShouldSucceed() {
        //act
        User userFound = userRepository.findUserByUserIdentifier_UserId(preSavedUser.getUserIdentifier().getUserId());

        //assert
        assertNotNull(userFound);
        assertThat(preSavedUser, samePropertyValuesAs(userFound));
    }

    @Test
    public void findByInvalidUserIdentifier_UserId_ShouldReturnNull() {
        //act
        User userFound = userRepository.findUserByUserIdentifier_UserId(preSavedUser.getUserIdentifier().getUserId() + 1);

        //assert
        assertNull(userFound);
    }

    @Test
    public void existsUserIdentifier_UserId_ShouldReturnTrue() {
        // act
        Boolean userFound = userRepository.existsByUserIdentifier_UserId(preSavedUser.getUserIdentifier().getUserId());
        // assert
        assertTrue(userFound);
    }

    @Test
    public void existsInvalidUserIdentifier_UserId_ShouldReturnFalse() {
        // act
        Boolean userFound = userRepository.existsByUserIdentifier_UserId(preSavedUser.getUserIdentifier().getUserId() + 1);
        // assert
        assertFalse(userFound);
    }

    @Test
    public void existsEmail_ShouldReturnTrue() {
        // act
        Boolean userFound = userRepository.existsByEmail(preSavedUser.getEmail());
        // assert
        assertTrue(userFound);
    }

    @Test
    public void existsInvalidEmail_ShouldReturnFalse() {
        // act
        Boolean userFound = userRepository.existsByEmail(preSavedUser.getEmail() + "1");
        // assert
        assertFalse(userFound);
    }

    @Test
    public void findByEmail_ShouldSucceed() {
        //act
        User userFound = userRepository.findByEmail(preSavedUser.getEmail());

        //assert
        assertNotNull(userFound);
        assertThat(preSavedUser, samePropertyValuesAs(userFound));
    }

    @Test
    public void findByInvalidEmail_ShouldReturnNull() {
        //act
        User userFound = userRepository.findByEmail(preSavedUser.getEmail() + "1");

        //assert
        assertNull(userFound);
    }

    @Test
    public void findByEmailAndPassword_ShouldSucceed() {
        //act
        User userFound = userRepository.findByEmailAndPassword(preSavedUser.getEmail(), preSavedUser.getPassword());

        //assert
        assertNotNull(userFound);
        assertThat(preSavedUser, samePropertyValuesAs(userFound));
    }

    @Test
    public void findByInvalidEmailAndPassword_ShouldReturnNull() {
        //act
        User userFound = userRepository.findByEmailAndPassword(preSavedUser.getEmail() + "1", preSavedUser.getPassword() + "1");

        //assert
        assertNull(userFound);
    }

    @Test
    public void findByEmailAndPassword_ShouldReturnNull() {
        //act
        User userFound = userRepository.findByEmailAndPassword(preSavedUser.getEmail(), preSavedUser.getPassword() + "1");

        //assert
        assertNull(userFound);
    }

    @Test
    public void saveNewUser_ShouldSucceed() {
        // arrange
        String firstName = "Josh";
        String lastName = "Din";
        String email = "josh.din@gmail.com";
        String phoneNumber = "5147890876";
        String password = "passWorrrrD$";

        //act
        User userSaved = userRepository.save(new User(firstName, lastName, email, phoneNumber, password));

        //assert
        assertNotNull(userSaved);
        assertNotNull(userSaved.getId());
        assertNotNull(userSaved.getUserIdentifier().getUserId());

        assertEquals(firstName, userSaved.getFirstName());
        assertEquals(lastName, userSaved.getLastName());
        assertEquals(email, userSaved.getEmail());
        assertEquals(phoneNumber, userSaved.getPhoneNumber());
        assertEquals(password, userSaved.getPassword());
    }

    @Test
    public void updateUser_ShouldSucceed() {
        // arrange
        String firstName = "Joshy";
        String lastName = "Diny";
        String email = "joshy.diny@gmail.com";
        String phoneNumber = "5147890878";
        String password = "passWor$";
        preSavedUser.setFirstName(firstName);
        preSavedUser.setLastName(lastName);
        preSavedUser.setEmail(email);
        preSavedUser.setPhoneNumber(phoneNumber);
        preSavedUser.setPassword(password);

        //act
        User userUpdated = userRepository.save(preSavedUser);

        //assert
        assertNotNull(userUpdated);
        assertThat(preSavedUser, samePropertyValuesAs(userUpdated));
    }

    @Test
    public void deleteUser_ShouldSucceed() {
        //act
        userRepository.delete(preSavedUser);

        //assert
        assertFalse(userRepository.existsByUserIdentifier_UserId(preSavedUser.getUserIdentifier().getUserId()));
    }
}