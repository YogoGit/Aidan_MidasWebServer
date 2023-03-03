package com.midaswebserver.midasweb.controllers;

import com.midaswebserver.midasweb.forms.LoginForm;
import com.midaswebserver.midasweb.services.LoginService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

/**
 * The LoginController works with all interactions by the client regarding logging in
 * Sending a form to a PostMethod with the client's information is the major use
 */
@Controller
public class LoginController {
    private static final Logger log = LoggerFactory.getLogger(LoginController.class);
    private final LoginService loginService;
    public LoginController(LoginService loginService) {this.loginService = loginService;}

    /**
     * loginGet sends the loginForm with the model to the client
     * @param model
     * @return "login" template
     */
    @GetMapping("/login")
    public String logingGet(Model model){
        model.addAttribute("loginForm", new LoginForm());
        log.info("A user has attempted to sign in"); //how can I make this better? add to a counter?
        return "login";
    }

    /**
     * loginPost takes the completed form for login, validates, and copies data to a User object
     * @param loginForm
     * @param result
     * @param attrs
     * @return either "loginsuccess" or "login"
     */
    @PostMapping("/login")
    public String loginPost(@Valid @ModelAttribute LoginForm loginForm, BindingResult result, RedirectAttributes attrs){
        if (result.hasErrors()){
            log.debug("{} has submitted a form which has errors", loginForm.getUsername());
            return "login";
        }
        if(!loginService.validateUser(loginForm)){
            result.addError(new ObjectError("globalError", "Username and password do not match known users"));
            log.info("{} was used for a login attempt that failed validation", loginForm.getUsername());
            return "/login";
        }
        attrs.addAttribute("username", loginForm.getUsername());
        log.info("{} has successfully logged in", loginForm.getUsername());//adding a time to this could be useful, or a more global logging system
        return "redirect:/loginSuccess";
    }

    /**
     * adds or updates persistent cookie
     * TODO rather than adding only username, add object that holds all of whats important
     * TODO much of this needs to be moved to a separate cookie class. We need a centralized location to work with cookies
     * @param username
     * @param model
     * @return the "loginSuccess" template
     */
    @GetMapping("/loginSuccess")
    public String loginSuccess(String username, Model model, HttpServletResponse response, HttpSession session){
        session.getId();
        Cookie cookie = new Cookie("username", username);
        cookie.setMaxAge(1 * 24 * 60 *60);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
        log.debug("cookie added or replaced on '{}' browser", username);
        log.debug("/loginSuccess has been connected to");
        model.addAttribute("username", username);
        return "loginSuccess";
    }

    /**
     * @return the "loginFailure" template
     */
    @GetMapping("/loginFailure")
    public String loginFailure(){return "loginFailure";}


    public static final String INDEX_TEMPLATE_VIEW_NAME = "index";

    private final Set<String> sessionIds = Collections.synchronizedSet(new HashSet<>());
    @GetMapping("/session")
    public ModelAndView sessionRequestCounts(HttpSession session) {

        this.sessionIds.add(session.getId());


        Map<String, Object> model = new HashMap<>();

        model.put("sessionType", session.getClass().getName());
        log.debug("sessionType: '{}'", session.getClass().getName());
        log.debug("Session ID is '{}'",session.getId());
        model.put("sessionCount", this.sessionIds.size());

        return new ModelAndView(INDEX_TEMPLATE_VIEW_NAME, model);
    }
}
