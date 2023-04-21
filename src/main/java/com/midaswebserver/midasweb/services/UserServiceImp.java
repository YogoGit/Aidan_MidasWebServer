package com.midaswebserver.midasweb.services;

import com.midaswebserver.midasweb.models.User.Symbol;
import com.midaswebserver.midasweb.models.User.User;
import com.midaswebserver.midasweb.repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author Aidan Scott
 * @since 0.0.1
 * UserService is the CRUD service for Users. the majority of user logic goes through the
 * UserServiceImp
 */
@Service
public class UserServiceImp implements UserService {
    private static final Logger log = LoggerFactory.getLogger(UserServiceImp.class);
    private final UserRepository userRepo;

    /**
     * Injected dependencies
     *
     * @param userRepo
     */
    public UserServiceImp(UserRepository userRepo) {
        this.userRepo = userRepo;
        log.debug("User Repository initialized in UserService");
    }

    /**
     * Adds user to the database
     * If User is already in the database (same username) then the method will return false
     *
     * @param user
     * @return true if user could be added, if there was a user of the same name or user is null, false
     */
    @Override
    public boolean add(User user) {
        if (user == null) {
            log.error("add: User is null");
            return false;
        }
        if (user.getUsername()==null ||  user.getEmail()==null || user.getPhoneNumber()==null || user.getHashedPassword()==null) {
            log.error("add: One of fields in user found to be null");
            return false;
        }
        if (user.getUsername().isBlank() ||  user.getEmail().isBlank() || user.getPhoneNumber().isBlank() || user.getHashedPassword().isBlank()) {
            log.error("add: One of fields in user found to be blank");
            return false;
        }
        if (!validateUniqueUsername(user.getUsername())) {
            log.error("add: '{}' has the same name in database", user.getUsername());
            return false;
        }
        userRepo.save(user);
        log.debug("add: '{}' has been added to the database", user.getUsername());
        return true;
    }

    /**
     * ValidateUniqueUsername checks to see if there is another person in the database with the same username
     *
     * @param username
     * @return boolean
     */
    @Override
    public boolean validateUniqueUsername(String username) {
        log.debug("validateUser: '{}' attempted login", getUserByUsername(username));
        // Always do the lookup in a case-insensitive manner (lower-casing the data).
        List<User> users = userRepo.findByUsernameIgnoreCase(username);

        // We expect 0 or 1, so if we get more than 1, bail out as this is an error we don't deal with properly.
        if (users.size() != 0) {
            log.debug("validateUniqueUsername: found {} users, invalid quantity. Valid = 1", users.size());
            return false;
        }
        return true;
    }

    /**
     * delete deletes a user from the database
     *
     * @param user
     * @return boolean
     */
    @Override
    public boolean delete(User user) {
        if (user == null) {
            log.error("Delete: null was passed to method");
            return false;
        }
        if (this.getUserByUsername(user.getUsername()) == null) {
            log.error("Delete: User '{}' was not found in database to delete", user.getUsername());
            return false;
        }
        userRepo.delete(user);
        log.debug("Delete: User '{}' was deleted from the database", user.getUsername());
        return true;
    }


    /**
     * Gets User by Id
     * TODO have validation and safe and expected returns;
     *
     * @param ID
     * @return User if user excists, returns null otherwise and if user passes null ID
     */
    @Override
    public User getUserById(Long ID) {
        if (ID == null) {
            return null;
        }
        try {
            return userRepo.findById(ID).get();
        } catch (Exception e) {
            log.error("getUserByID: id, '{}', didnt have corresponding user", ID, e);
        }
        return null;
    }

    /**
     * Gets user with the corresponding name
     *
     * @param userName
     * @return user with corresponding name, or null if no user was found
     */
    @Override
    public User getUserByUsername(String userName) {
        if (userName != null) {
            try {
                List<User> users = userRepo.findByUsernameIgnoreCase(userName);
                if (users.size() == 1) {
                    return users.get(0);
                } else {
                    log.warn("GetUserByName: 0 or 1 < users found with name '{}'", userName);
                }
            } catch (Exception e) {
                log.info("getUserByName:", e);
            }
        }
        return null;
    }

    /**
     * Gets Id by corresponding username
     *
     * @param username
     * @return the ID of the user or null if there isn't a user in the database with that username or if there are more than one user
     */
    @Override
    public Long getIdByUsername(String username) {
        List<User> users = userRepo.findByUsernameIgnoreCase(username);
        if (username == null) {
            log.error("getIDByUser: passed username was null");
            return null;
        }
        if (users.size() == 0) {
            log.info("getIDByUser: no user found with username of '{}'", username);
            return null;
        }
        if (users.size() > 1) {
            log.error("getIDByUser: two users with username '{}' have been found", username);
            return null;
        }
        return users.get(0).getId();
    }

    /**
     * Updates the user in database, make sure that the param has the original user ID set as its ID,
     * the function will return null if not
     *
     * @param user
     * @return boolean
     */
    @Override
    public boolean update(User user) {
        if (user == null) {
            log.error("update: passed username was null");
            return false;
        }
        if (this.getUserById(user.getId()) == null) {
            log.warn("update: D of user '{}' didn't match anything in database", user.getUsername());
            return false;
        }
        log.debug("update: User '{}' has been updated", user.getUsername());
        userRepo.save(user);
        return true;
    }

    /**
     * Adds a symbol to a user. This will create a symbol object linked to the user object
     * which can be used to retrieve the saved ticker
     * WIll return false if user or ticker is null
     * if ticker is invalid > 4, return false
     * if User doesn't exist, return false
     * if ticker is already saved, return true
     * if ticker is added, return true
     * all else fails, return false
     *
     * @param user
     * @param ticker like 'UEC'
     * @return boolean based on the success of the transaction
     */
    @Transactional
    @Override
    public boolean addSymbolToUser(User user, String ticker) {
        //validation
        if (user == null || ticker == null) {
            log.error("addSymbolToUser: user, '{}', or symbol, '{}', was found to be null", user, ticker);
            return false;
        }
        if (ticker.getBytes().length > 4) {
            log.error("addSymbolToUser: symbol, '{}', was longer than expected, 4 characters", ticker);
            return false;
        }
        if (!userRepo.existsById(user.getId())) {
            log.error("addSymbolToUser: user, '{}', with id, '{}', was not found in database", user.getUsername(), user.getId());
            return false;
        }
        //logic
        for (Symbol symbolSaved : user.getSymbol()) {
            if (symbolSaved.getTicker().equalsIgnoreCase(ticker)) {
                log.debug("addSymbolToUser: user, '{}', attempted to add ticker, '{}', that was added previously", user.getUsername(), ticker);
                return true;
            }
        }
        user.addTicker(ticker, user);
        update(user);
        for (Symbol symbolSaved : user.getSymbol()) {
            if (symbolSaved.getTicker().equalsIgnoreCase(ticker)) {
                log.debug("addSymbolToUser: user, '{}', added ticker , '{}', to the database", user.getUsername(), ticker);
                return true;
            }
        }
        log.error("addSymbolToUser: user, '{}', failed to add ticker , '{}', should have found ticker in database", user.getUsername(), ticker);
        return false;
    }

    /**
     * takes request and finds the user's Ip. Used in place of user id when logging
     *
     * @param request {@link HttpServletRequest} takes the servlet request received from a post method
     * @return remote IP address
     */
    @Override
    public String getClientIp(HttpServletRequest request) {
        String remoteAddr = "";

        if (request != null) {
            remoteAddr = request.getHeader("X-FORWARDED-FOR");
            if (remoteAddr == null || "".equals(remoteAddr)) {
                remoteAddr = request.getRemoteAddr();
            }
        }

        return remoteAddr;
    }
}
