package com.midaswebserver.midasweb.services;

import com.midaswebserver.midasweb.forms.LoginForm;

/**
 * @version 0.0.1
 * LoginServiceImp validates user data relating to login
 * TODO consolidate loginService and UserService methods
 * @Author Aidan Scott
 * @since 0.0.1
 */
public interface LoginService {
    /**
     * Given a loginForm, determine if the information provided is valid, and the user exists in the system.
     *
     * @param form - Data containing user login information, such as username and password.
     * @return true if data exists and matches what's on record, false otherwise
     */
    boolean validateUser(LoginForm form);//change to two paramaters rather than the form
}
