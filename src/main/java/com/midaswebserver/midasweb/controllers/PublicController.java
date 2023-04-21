package com.midaswebserver.midasweb.controllers;

import com.midaswebserver.midasweb.models.User.User;
import com.midaswebserver.midasweb.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PublicController {
    private static final Logger log = LoggerFactory.getLogger(PublicController.class);
    private final UserService userService;

    /**
     * Injects services into controller
     *
     * @param userService
     */
    public PublicController(UserService userService){
        this.userService = userService;
    }
    /**
     * Routs to index page
     *
     * @param model
     * @param session {@link HttpSession}
     * @return "index" template
     */
    @GetMapping("/")
    public String index(Model model, HttpSession session, HttpServletRequest request) {
        User baseUser;
        log.debug("index: User from '{}' has accessed Index page", userService.getClientIp(request));
        if (!session.isNew() && session.getAttribute("UserId") != null) {
            baseUser = userService.getUserById(Long.parseLong(session.getAttribute("UserId").toString()));
            log.debug("index: User '{}' from '{}' has accessed Index page",
                    session.getAttribute("UserId").toString(), userService.getClientIp(request));
        } else {
            baseUser = new User();
            baseUser.setUsername("New User");
        }
        model.addAttribute("user", baseUser);
        return "index";
    }

}
