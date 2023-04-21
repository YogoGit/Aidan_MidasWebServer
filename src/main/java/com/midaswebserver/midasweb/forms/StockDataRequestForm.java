package com.midaswebserver.midasweb.forms;

import com.crazzyghost.alphavantage.parameters.Interval;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * @version 0.0.1
 * StocDataRequestForm is used in any situation when a client calls for stock data
 * @Author Aidan Scott
 * @since 0.0.1
 */
public class StockDataRequestForm {
    @NotNull
    @NotBlank(message = "Please enter ticker symbol")
    @Size(min = 1, max = 4)
    public String ticker;
    @NotNull(message = "Must add an interval")
    public Interval interval;
    public StockDataRequestForm() {
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public Interval getInterval() {
        return interval;
    }

    public void setInterval(Interval interval) {
        this.interval = interval;
    }

    @Override
    public String toString() {
        return "StockDataRequestForm{" +
                "ticker='" + ticker + '\'' +
                ", interval=" + interval +
                '}';
    }
}
