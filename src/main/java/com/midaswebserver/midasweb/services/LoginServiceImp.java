package com.midaswebserver.midasweb.services;

import com.midaswebserver.midasweb.forms.LoginForm;
import com.midaswebserver.midasweb.models.User.User;
import com.midaswebserver.midasweb.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @version 0.0.1
 * LoginServiceImp validates user data relating to login
 * TODO consolidate loginService and UserService methods
 * @Author Aidan Scott
 * @since 0.0.1
 */
@Service
public class LoginServiceImp implements LoginService {
    private static final Logger log = LoggerFactory.getLogger(LoginServiceImp.class);
    private final UserRepository loginRepo;
    private final HashService hashService;
    private final UserService userService;

    /**
     * Injected dependencies
     *
     * @param loginRepo
     * @param hashService
     * @param userService
     */
    public LoginServiceImp(UserRepository loginRepo, HashService hashService, UserService userService) {
        this.loginRepo = loginRepo;
        this.hashService = hashService;
        this.userService = userService;
    }

    /**
     * Given a loginForm, determine if the information provided is valid, and the user exists in the system.
     * //TODO break up validation into component methods to test better
     *
     * @param loginForm - Data containing user login information, such as username and password.
     * @return true if data exists and matches what's on record, false otherwise
     */
    @Override
    public boolean validateUser(LoginForm loginForm) {
        log.debug("validateUser:'{}' attempting login", userService.getUserByUsername(loginForm.getUsername()));
        // Always do the lookup in a case-insensitive manner (lower-casing the data).
        List<User> users = loginRepo.findByUsernameIgnoreCase(loginForm.getUsername());

        // We expect 0 or 1, so if we get more than 1, bail out as this is an error we don't deal with properly.
        if (users.size() != 1) {
            log.debug("validateUser: Found '{}' user(s), invalid quantity of users, should only have 1 user", users.size());
            return false;
        }
        User u = users.get(0);
        if (!hashService.checkMatch(loginForm.getPassword(), u.getHashedPassword())) {
            log.debug("validateUser: Given password doesnt match stored password");
            return false;
        }

        // User exists, and the provided password matches the hashed password in the database.
        log.debug("validateUser: User '{}', successful login", userService.getUserByUsername(loginForm.getUsername()));
        return true;
    }
}

