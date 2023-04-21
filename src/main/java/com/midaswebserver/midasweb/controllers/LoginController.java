package com.midaswebserver.midasweb.controllers;

import com.midaswebserver.midasweb.forms.LoginForm;
import com.midaswebserver.midasweb.services.LoginService;
import com.midaswebserver.midasweb.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * @version 0.0.1
 * The LoginController works with all interactions by the client regarding logging in, logging out
 * uses {@link LoginService} and {@link UserService} heavily to interact with user repository and for business logic
 * @Author Aidan Scott
 * @sinse 0.0.1
 */
@Controller
public class LoginController {
    private static final Logger log = LoggerFactory.getLogger(LoginController.class);
    private final LoginService loginService;
    private final UserService userService;

    /**
     * Injects services into controller
     *
     * @param loginService
     * @param userService
     */
    public LoginController(LoginService loginService, UserService userService) {
        this.loginService = loginService;
        this.userService = userService;
    }

    /**
     * loginGet sends the loginForm with the model to the client
     * also creates cookie that holds previous uri
     *
     * @param model
     * @return "login" template, which houses all info needed for login
     */
    @GetMapping("/login")
    public String loginGet(Model model, HttpServletResponse response, HttpServletRequest request) {
        model.addAttribute("loginForm", new LoginForm());
        log.debug("loginGet: Client '{}' connected to /login", userService.getClientIp(request));
        Cookie preLoginUriCookie = new Cookie("preLoginUri", request.getHeader("referer"));
        preLoginUriCookie.setMaxAge(60 * 4); //sets cookie's life to 10 seconds
        response.addCookie(preLoginUriCookie);
        return "login";
    }

    /**
     * loginPost takes the completed form for login, validates, and copies data to a User object
     * Creates session for user
     *
     * @param loginForm {@link LoginForm} returned loginform with filled out user data
     * @param result    {@link BindingResult} if loginForm data is valid, what errors it contains
     * @param attrs
     * @param request   {@link HttpServletRequest}
     * @return either "index" or redirect user to previous page
     */
    @PostMapping("/login")
    public String loginPost(@Valid @ModelAttribute @CookieValue("preLoginUri") String preLoginUri, LoginForm loginForm, BindingResult result, RedirectAttributes attrs, HttpServletRequest request, HttpSession session) {
        if (loginForm.getUsername().equals("") || loginForm.getPassword().equals("")) {
            result.addError(new ObjectError("globalError", "Make sure to fill all fields"));
            log.debug("loginPost: Attempted login with empty fields from '{}' with username '{}'", userService.getClientIp(request), loginForm.getUsername());
            return "login";
        }
        if (result.hasErrors()) {
            log.debug("LoginPost: Form from '{}' had errors: '{}'", userService.getClientIp(request), result.getAllErrors());
            return "login";
        }
        if (!loginService.validateUser(loginForm)) {
            result.addError(new ObjectError("globalError", "Username and password do not match known users"));
            log.debug("loginPost: Failed Attempted validation from '{}' with username '{}', invalid Username or password",
                    userService.getClientIp(request), loginForm.getUsername());
            return "login";
        }
        attrs.addAttribute("username", loginForm.getUsername());
        log.debug("loginPost: User '{}' has successfully logged in from '{}'", userService.getUserByUsername(loginForm.getUsername()), userService.getClientIp(request));
        //setting up session for valid user
        session.setAttribute("UserId", userService.getIdByUsername(loginForm.getUsername()));
        log.debug("loginSuccess: User '{}' was given session of ID '{}'", userService.getUserByUsername(loginForm.getUsername()), session.getId());
        //Redirecting user to correct location
        if (preLoginUri == null) {
            log.debug("getTickerData:'{}', no address found in preLoginUri, redirect failed", userService.getClientIp(request));
            return "redirect:/user/home";
        }
        log.debug("loginPost: User '{}' has been redirected to '{}'", userService.getUserByUsername(loginForm.getUsername()), preLoginUri);
        return "redirect:" + preLoginUri;
    }

    /**
     * logs out user, invalidates session
     *
     * @param request {@link HttpServletRequest}
     * @param session {@link HttpSession}
     * @return the "index" template
     */
    @GetMapping("/logout")
    public String logout(HttpSession session, HttpServletRequest request) {
        log.debug("logout: User '{}', session ID '{}', location '{}', logged out",
                userService.getUserById((long) session.getAttribute("UserId")), session.getId(), userService.getClientIp(request));
        session.invalidate();
        return "redirect:/";
    }
}
