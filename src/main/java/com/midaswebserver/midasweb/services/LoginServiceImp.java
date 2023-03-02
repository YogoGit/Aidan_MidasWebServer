package com.midaswebserver.midasweb.services;

import com.midaswebserver.midasweb.forms.LoginForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.midaswebserver.midasweb.repositories.UserRepository;
import com.midaswebserver.midasweb.models.User;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * LoginServiceImp validates user data relating to login
 * TODO consolidate loginService and UserService methods
 */
@Service
public class LoginServiceImp implements LoginService {
    private static final Logger log = LoggerFactory.getLogger(LoginServiceImp.class);
    private final UserRepository loginRepo;
    public LoginServiceImp(UserRepository loginRepo) {
        this.loginRepo = loginRepo;
    }

    /**
     * Given a loginForm, determine if the information provided is valid, and the user exists in the system.
     *
     * @param loginForm - Data containing user login information, such as username and password.
     * @return true if data exists and matches what's on record, false otherwise
     */
    @Override
    public boolean validateUser(LoginForm loginForm) {
        log.info("validateUser: user '{}' attempted login", loginForm.getUsername());
        // Always do the lookup in a case-insensitive manner (lower-casing the data).
        List<User> users = loginRepo.findByUsernameIgnoreCase(loginForm.getUsername());

        // We expect 0 or 1, so if we get more than 1, bail out as this is an error we don't deal with properly.
        if (users.size() != 1) {
            log.debug("validateUser: found {} users", users.size());
            return false;
        }
        User u = users.get(0);
        // XXX - Using Java's hashCode is wrong on SO many levels, but is good enough for demonstration purposes.
        // NEVER EVER do this in production code!
        final String userProvidedHash = Integer.toString(loginForm.getPassword().hashCode());//blowfish is a 8-9 so do that
        if (!u.getHashedPassword().equals(userProvidedHash)) {
            log.info("validateUser: password !match");
            return false;
        }

        // User exists, and the provided password matches the hashed password in the database.
        log.info("validateUser: successful login");
        return true;
    }
}

