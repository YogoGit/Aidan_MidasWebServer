package com.midaswebserver.midasweb.controllers;

import com.midaswebserver.midasweb.forms.StockDataRequestForm;
import org.springframework.ui.Model;
import com.midaswebserver.midasweb.forms.UserForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NavigationController {
    private static final Logger log = LoggerFactory.getLogger(NavigationController.class);
    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/user/home")
    public String home(Model model) {
        model.addAttribute("stockDataRequestForm", new StockDataRequestForm());
        return "home"; }


}