package com.midaswebserver.midasweb.apiModels;

import com.crazzyghost.alphavantage.timeseries.response.StockUnit;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @version 0.0.1
 * Ticker is the object used to house all the information from a ticker query
 * This class is a custom implementation of {@link com.crazzyghost.alphavantage crazzyghost alphabantage}
 * All of the subsequent code is from <a href="https://github.com/crazzyghost">crazzyghost</a> using the alphavantage library
 * @Author crazzyghost
 * @since 0.0.1
 */
public class Ticker {

    @NotNull
    private MetaData metaData;

    @NotNull
    private List<StockUnit> timeSeries = new ArrayList<StockUnit>();

    public Ticker() {
    }

    public MetaData getMetaData() {
        return metaData;
    }

    public void setMetaData(MetaData metaData) {
        this.metaData = metaData;
    }

    public List<StockUnit> getTimeSeries() {
        return timeSeries;
    }

    /**
     * setTimeSeries takes a list of foreign objects and sets it to an internal value
     *
     * @param timeSeries
     */
    public void setTimeSeries(List<StockUnit> timeSeries) {
        this.timeSeries = timeSeries;
    }

    @Override
    public String toString() {
        return "Ticker{" +
                "metaData=" + metaData +
                ", timeSeries=" + timeSeries +
                '}';
    }
}

