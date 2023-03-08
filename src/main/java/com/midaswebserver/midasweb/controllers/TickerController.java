package com.midaswebserver.midasweb.controllers;

import com.crazzyghost.alphavantage.parameters.OutputSize;
import com.midaswebserver.midasweb.apiModels.Ticker;
import com.midaswebserver.midasweb.forms.StockDataRequestForm;
import com.midaswebserver.midasweb.models.User.Settings;
import com.midaswebserver.midasweb.models.User.User;
import com.midaswebserver.midasweb.services.TickerService;
import com.midaswebserver.midasweb.services.TickerServiceImp;
import com.midaswebserver.midasweb.services.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Stack;

/**
 * TickerController will work with stock data and related forms like the StockDataRequestForm
 */
@Controller
public class TickerController {
    private static final Logger log = LoggerFactory.getLogger(TickerController.class);
    private TickerService tickerService = new TickerServiceImp();
    @Autowired
    private UserService userService;

    /**
     * GetTickerData takes a StockDataRequestForm and returns the equivalent stock data
     * will return a null value if anything went wrong.
     * TODO errors should be returned rather than a null value, the client should understand what went wrong
     * @param stockDataRequestForm
     * @param result
     * @return json of converted ticker object
     */
    @GetMapping("/ticker/data")
    public ResponseEntity getTickerData(@Valid @ModelAttribute StockDataRequestForm stockDataRequestForm, BindingResult result, HttpSession session){
        if(result.hasErrors()){
            log.debug("Found Errors: '{}'", result.getAllErrors());
            return ResponseEntity.ok(result.getAllErrors());
        }
        Ticker ticker = tickerService.getTimeSeriesInfo(stockDataRequestForm.getTicker(), stockDataRequestForm.getInterval(), OutputSize.COMPACT);
        //adds called ticker to tickers that have been called before
        if(ticker.getMetaData().getSymbol()!=null) {
            User user = userService.getUserByID(Long.parseLong(session.getId()));
            Settings settings = user.getSettings();
            Stack tickerStack = settings.getTickers();
            tickerStack.push(ticker.getMetaData().getSymbol());
            settings.setTickers(tickerStack);
            user.setSettings(settings);
        }
        return ResponseEntity.ok(ticker);
    }
}
