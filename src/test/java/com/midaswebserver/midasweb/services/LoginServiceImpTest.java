package com.midaswebserver.midasweb.services;

import com.midaswebserver.midasweb.forms.LoginForm;
import com.midaswebserver.midasweb.models.User.User;
import com.midaswebserver.midasweb.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.springframework.test.util.AssertionErrors.*;

@SpringBootTest
public class LoginServiceImpTest {
    //get services and other required objects to test or to aid the testing
    @Autowired
    private LoginService loginService;
    @Autowired
    private UserRepository userRepo;

    @BeforeEach
    public void beforeTest() {
        String username = "testuser";
        String password = "testpass";
        User fakeUser = new User(username, Integer.toString(password.hashCode()));//login (the name) might want to be changed as it is a little confusing
        assertNotNull("UserRepository must be injected", userRepo);
        assertNotNull("loginService must be injected", loginService);

        // Ensure dummy record is in the DB
        final List<User> users =
                userRepo.findByUsernameIgnoreCase(username);
        if (users.isEmpty())
            userRepo.save(fakeUser);
    }

    @Test//happy
    public void validateUserSuccessTest1() {
        String username = "testuser";
        String password = "testpass";
        final LoginForm form = new LoginForm(username, password);
        assertTrue("validateUserSuccessTest: didnt succeed with valid user/pass info", loginService.validateUser(form));
    }

    @Test//happy
    public void validateUserSuccessTest2() {
        String username = "HelloWorld123455<>";
        String password = "<>HelloWorld123455<>/";
        final LoginForm form = new LoginForm(username, password);
        assertFalse("validateUserSuccessTest: didn't succeed using valid user/pass info", loginService.validateUser(form));
    }

    @Test//crappy
    public void validateUserExistingUserInvalidPasswordTest() {
        String username = "testuser";
        String password = "testpass";
        final LoginForm form = new LoginForm(username, password + "extra");
        assertFalse("validateUserExistingUserInvalidPasswordTest: should fail using a valid user, invalid pass", loginService.validateUser(form));
    }

    @Test//crappy
    public void validateUserInvalidUserValidPasswordTest() {
        String username = "testuser";
        String password = "testpass";
        final LoginForm form = new LoginForm(username + "not", password);
        assertFalse("validateUserInvalidUserValidPasswordTest: should fail using an invalid user, valid pass", loginService.validateUser(form));
    }

    @Test//crappy
    public void validateUserInvalidUserAndPasswordTest() {
        String username = "testuser";
        String password = "testpass";
        final LoginForm form = new LoginForm(username + "not", password + "<><>UWU<><>");
        assertFalse("validateUserInvalidUserValidPasswordTest: should fail using an invalid user, valid pass", loginService.validateUser(form));
    }

    @Test//crazy
    public void validateUserNulls() {
        final LoginForm form = new LoginForm(null, null);
        assertFalse("validateUserInvalidUserInvalidPasswordTest: should fail using an invalid user, valid pass", loginService.validateUser(form));
    }

    @Test//crazy
    public void validateUserBlanks() {
        final LoginForm form = new LoginForm();
        assertFalse("validateUserInvalidUserInvalidPasswordTest: should fail using an invalid user, valid pass", loginService.validateUser(form));
    }

    @Test//crazy
    public void validateUserLong() {
        final LoginForm form = new LoginForm("I WISH I WOULD PUT THE ENTIRE SCRIPT OF MCBETH IN HERE", "THIS SADLY ISNT THE ENTIRE SCRIPT");
        assertFalse("validateUserInvalidUserInvalidPasswordTest: should fail using an invalid user, valid pass", loginService.validateUser(form));
    }
}