package com.midaswebserver.midasweb.controllers;

import com.crazzyghost.alphavantage.parameters.OutputSize;
import com.crazzyghost.alphavantage.timeseries.response.StockUnit;
import com.midaswebserver.midasweb.apiModels.Ticker;
import com.midaswebserver.midasweb.forms.StockDataRequestForm;
import com.midaswebserver.midasweb.models.User.Symbol;
import com.midaswebserver.midasweb.models.User.User;
import com.midaswebserver.midasweb.services.TickerService;
import com.midaswebserver.midasweb.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.*;

/**
 * @Author Aidan Scott
 * @since 0.0.1
 * @version 0.0.1
 * UserPersonal manages the endpoints for general locations
 * This includes the index page and the user/home page
 * In the future general endpoints can be split up into more specific areas (admin, management, and so on)
 * Uses {@link UserService} heavily to get userdata used in logs and sessions
 */
@Controller
public class UserPersonal {
    private static final Logger log = LoggerFactory.getLogger(UserPersonal.class);
    @Autowired
    private UserService userService;
    @Autowired
    private TickerService tickerService;

    /**
     * Routs the user to their home. Uses sessions to provide a custom experience
     * Adds the {@link User} and {@link Symbol} to the model
     * @param session {@link HttpSession}
     * @param model
     * @return the "home" template
     */
    @Transactional
    @GetMapping("/user/home")
    public String home(HttpSession session,  Model model) {
        //session/user validation if user is logged in
        Object userId = session.getAttribute("UserId");
        if (userId == null)
            return "redirect:/login";

        //exposing and setting standard model attributes
        User user = userService.getUserById((Long)(userId));
        model.addAttribute("user", user);
        //other attributes
        Symbol[] symbols = user.getSymbol().toArray(new Symbol[user.getSymbol().size()]);
        model.addAttribute("userSettings", symbols);
        log.debug("home: User '{}', data added to form ", session.getAttribute("UserId"));
        model.addAttribute("stockDataRequestForm", new StockDataRequestForm());
        return "home";
    }

    /**
     * GetTickerData takes a StockDataRequestForm and returns the equivalent stock data
     * will return a null value if anything went wrong.
     * TODO errors should be returned rather than a null value, the client should understand what went wrong
     * @param stockDataRequestForm {@link StockDataRequestForm}
     * @param result {@link BindingResult}
     * @param session {@link HttpSession}
     * @return previous url the user had, after it adds ticker data to their session
     */
    @Transactional
    @PostMapping ("/user/home")
    public String getTickerData(@Valid @ModelAttribute StockDataRequestForm stockDataRequestForm, BindingResult result, HttpSession session, Model model, HttpServletRequest request){
        //session user validation if user is logged in
        Object userId = session.getAttribute("UserId");
        if (userId == null)
            return "redirect:/login";
        if (request.getHeader("referer") == null ){
            log.debug("getTickerData:'{}', Searched header, but didn't find a redirect address", session.getAttribute("UserId"));
            return "redirect:/index";
        }
        User user = userService.getUserById((Long)(userId));
        if(result.hasErrors()){
            log.debug("getTickerData:'{}', form had errors '{}'", session.getAttribute("UserId"), result.getAllErrors());
            return "redirect:/user/home";
        }
        Ticker ticker = tickerService.getTimeSeriesInfo(stockDataRequestForm.getTicker(), stockDataRequestForm.getInterval(), OutputSize.COMPACT);
        //adds called ticker to tickers that have been called before
        if(ticker==null){
            log.warn("getTickerData:'{}', Searched for ticker, '{}', bad data given, recieved bad data", session.getAttribute("UserId"), stockDataRequestForm.getTicker());
            return "redirect:/user/home";
        }
        if(ticker.getMetaData() != null || ticker.getMetaData().getSymbol() != null) {
            String symbol = ticker.getMetaData().getSymbol();
            boolean addedTicker = userService.addSymbolToUser(user, symbol);
            log.debug("getTickerData:'{}', Searched Ticker: '{}', added ticker, '{}'", session.getAttribute("UserId"), symbol, addedTicker);
        }
        else{ // this is if the incoming data isnt valid, this is probably because the wrong ticker was sent to the api
            log.warn("getTickerData:'{}', Searched for ticker, '{}', bad data given, recieved bad data", session.getAttribute("UserId"), stockDataRequestForm.getTicker());
            stockDataRequestForm.setTicker("invalid");//I dont know if they will be able to see this
            return "redirect:/user/home";
        }
        //exposing and setting standard model attributes
        model.addAttribute("user", user);
        //convert stock data to data chart.js can use
        List<Map<String, Object>> data = tickerService.tickerToDataPoints(ticker);
        //other attributes
        Symbol[] symbols = user.getSymbol().toArray(new Symbol[user.getSymbol().size()]);
        model.addAttribute("userSettings", symbols);
        model.addAttribute("data", data);
        return "dataDisplay";
    }
}