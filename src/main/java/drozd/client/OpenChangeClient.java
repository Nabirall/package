package drozd.client;

import drozd.model.Exchange;


public interface OpenChangeClient {

    Exchange getLatestRates(String appId);

    Exchange getHistoricalRates(String date, String appId);
}
