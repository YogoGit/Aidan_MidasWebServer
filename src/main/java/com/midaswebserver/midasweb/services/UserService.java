package com.midaswebserver.midasweb.services;

import com.midaswebserver.midasweb.models.User.User;
import jakarta.servlet.http.HttpServletRequest;

/**
 * @version 0.0.1
 * UserService is the CRUD service for Users. the majority of user logic goes through the
 * UserServiceImp
 * @Author Aidan Scott
 * @since 0.0.1
 */
public interface UserService {
    /**
     * Adds user to the database
     * If User is already in the database (same username) then the method will return false
     *
     * @param user
     * @return true if user could be added, if there was a user of the same name or user is null, false
     */
    boolean add(User user);

    /**
     * ValidateUniqueUsername checks to see if there is another person in the database with the same username
     *
     * @param username
     * @return boolean
     */
    boolean validateUniqueUsername(String username);

    /**
     * delete deletes a user from the database
     * TODO make sure the process succeeded
     *
     * @param user
     * @return boolean
     */
    boolean delete(User user);

    /**
     * Gets User by Id
     * TODO have validation and safe and expected returns;
     *
     * @param ID
     * @return User if user excists, returns null otherwise and if user passes null ID
     */
    User getUserById(Long ID);

    /**
     * Gets user with the corresponding name
     *
     * @param userName
     * @return user with corresponding name, or null if no user was found
     */
    User getUserByUsername(String userName);

    /**
     * Gets Id by corresponding username
     *
     * @param username
     * @return the ID of the user or null if there isn't a user in the database with that username or if there are more than one user
     */
    Long getIdByUsername(String username);

    /**
     * Updates the user in database, make sure that the param has the original user ID set as its ID,
     * the function will return null if not
     *
     * @param user
     * @return boolean
     */
    boolean update(User user);

    /**
     * Adds a symbol to a user. This will create a symbol object linked to the user object
     * which can be used to retrieve the saved ticker
     *
     * @param user
     * @param ticker like 'UEC'
     * @return boolean based on the success of the transaction
     */
    boolean addSymbolToUser(User user, String ticker);

    /**
     * takes request and finds the user's Ip. Used in place of user id when logging
     * TODO centralize getClientIp into one method {See UserController} to see other method
     *
     * @param request {@link HttpServletRequest} takes the servlet request received from a post method
     * @return remote IP address
     */
    String getClientIp(HttpServletRequest request);
}
