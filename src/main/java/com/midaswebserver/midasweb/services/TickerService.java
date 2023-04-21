package com.midaswebserver.midasweb.services;

import com.crazzyghost.alphavantage.parameters.Interval;
import com.crazzyghost.alphavantage.parameters.OutputSize;
import com.midaswebserver.midasweb.apiModels.Ticker;

import java.util.List;
import java.util.Map;

/**
 * @version 0.0.1
 * TickerService manages API requests to third party ticker databases.
 * Alpha Advantage is being queried
 * Webull will be queried in the future for realtime stock data
 * @Author Aidan Scott
 * @since 0.0.1
 */
public interface TickerService {
    /**
     * getTimeSeriesInfo takes params for the stock query for the AlphaVantage api
     *
     * @param symbol,     the symbol for the stock:IBM
     * @param interval,   the amount of time in between each record
     *                    Only 1min, 5min, 15min, 30min, 60min are valid
     * @param outputSize, either FULL, shorter more recent data or COMPACT, full length record
     * @return ticker object with requested data, will return null if input was invalid or errors occurred
     */
    Ticker getTimeSeriesInfo(String symbol, Interval interval, OutputSize outputSize);

    /**
     * Turns ticker data into a list of points data and high price
     *
     * @param ticker
     * @return list of points
     */
    List<Map<String, Object>> tickerToDataPoints(Ticker ticker);
}
