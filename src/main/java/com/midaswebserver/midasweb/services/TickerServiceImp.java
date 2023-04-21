package com.midaswebserver.midasweb.services;

import com.crazzyghost.alphavantage.AlphaVantage;
import com.crazzyghost.alphavantage.Config;
import com.crazzyghost.alphavantage.parameters.Interval;
import com.crazzyghost.alphavantage.parameters.OutputSize;
import com.crazzyghost.alphavantage.timeseries.response.StockUnit;
import com.crazzyghost.alphavantage.timeseries.response.TimeSeriesResponse;
import com.midaswebserver.midasweb.apiModels.MetaData;
import com.midaswebserver.midasweb.apiModels.Ticker;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Author Aidan Scott
 * @since 0.0.1
 * TickerService manages API requests to third party ticker databases.
 * Alpha Advantage is being queried
 * //TODO make apiKey a environment variable when containerizing
 * Webull will be queried in the future for realtime stock data
 */
@Service
public class TickerServiceImp implements TickerService {
    private static final Logger log = LoggerFactory.getLogger(TickerServiceImp.class);
    private static final String apiKey = "XIHOPJON41H6MNHT"; //I think this should be secret and have some logic behind it so it isnt found

    /**
     * TickerServiceImp sets up the apiKey and timeout params in config for AlphaVantage wrapper
     * The AlphaVantage wrapper can only be called 5 times a second and 500 times a day
     */
    public TickerServiceImp() {
        Config cfg = Config.builder()
                .key(apiKey)
                .timeOut(10)
                .build();
        AlphaVantage.api().init(cfg);
    }

    /**
     * getTimeSeriesInfo takes params for the stock query for the AlphaVantage api
     *
     * @param symbol,     the symbol for the stock:IBM
     * @param interval,   the amount of time in between each record
     *                    Only 1min, 5min, 15min, 30min, 60min are valid
     * @param outputSize, either FULL, shorter more recent data or COMPACT, full length record
     * @return ticker object with requested data, will return null if input was invalid or errors occurred
     */
    @Override
    public Ticker getTimeSeriesInfo(String symbol, Interval interval, OutputSize outputSize) {
        log.debug("getTimeSeriesInfo: '{}' at '{}' with '{}' output size was queried", symbol, interval, outputSize);
        TimeSeriesResponse timeSeries = AlphaVantage.api()
                .timeSeries()
                .intraday()
                .forSymbol(symbol)
                .interval(interval)
                .outputSize(outputSize)
                .fetchSync();
        Ticker ticker = convertToInternalObject(timeSeries);
        if (!(timeSeries.getErrorMessage() == null)) {
            log.info("getTimeSeriesInfo: Stock Query Error '{}'", timeSeries.getErrorMessage());
            ticker = null;
        } else if (!validateTicker(ticker)) {
            log.debug("getTimeSeriesInfo: Stock Query Data Invalid");
            ticker = null;
        }
        return ticker;
    }

    /**
     * Turns ticker data into a list of points data and high price
     *
     * @param ticker
     * @return list of points
     */
    @Override
    public List<Map<String, Object>> tickerToDataPoints(Ticker ticker) {
        List<Map<String, Object>> data = new ArrayList<>();
        if (ticker == null) {
            log.error("tickerToDataPoints: Ticker invalid, was found to be null");
            return data;
        }
        if (ticker.getTimeSeries() == null) {
            log.error("tickerToDataPoints: Ticker invalid, time series was found to be null");
            return data;
        }
        for (StockUnit stockUnit : ticker.getTimeSeries()) {
            Map<String, Object> point = new HashMap<>();
            point.put("x", stockUnit.getDate());
            point.put("y", stockUnit.getHigh());
            data.add(point);
        }
        return data;
    }

    /**
     * validateTicker uses the internal spring validation to validate objects with validation annotations
     *
     * @param ticker {@link Ticker}
     * @return boolean, true or false depending on validity
     */
    private boolean validateTicker(Ticker ticker) {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<Ticker>> violations = validator.validate(ticker);
        if (!violations.isEmpty()) {
            log.debug("validateTicker: Violation in ticker: '{}' of '{}'", ticker, violations);
            return false;
        }
        return true;
    }

    /**
     * convertToInternalObject changes a TimeSeriesResponse object to a native ticker
     * object that can be locally validated
     *
     * @param timeSeries {@link TimeSeriesResponse}
     * @return ticker, newly formed by the method
     */
    private Ticker convertToInternalObject(TimeSeriesResponse timeSeries) {
        Ticker ticker = new Ticker();
        MetaData metaData = new MetaData();
        ticker.setTimeSeries(timeSeries.getStockUnits());
        com.crazzyghost.alphavantage.timeseries.response.MetaData responseMetaData = timeSeries.getMetaData();
        metaData.setData(responseMetaData.getInformation(), responseMetaData.getSymbol(),
                responseMetaData.getLastRefreshed(), responseMetaData.getTimeZone(),
                responseMetaData.getInterval(), responseMetaData.getOutputSize());
        ticker.setMetaData(metaData);
        return ticker;
    }
}
