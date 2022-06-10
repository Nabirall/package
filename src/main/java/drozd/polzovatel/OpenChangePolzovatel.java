package drozd.polzovatel;

import drozd.service.ChangeService;
import drozd.shablon.Exchange;

public interface OpenChangePolzovatel {

    Exchange getLatestRates(String appId);

    Exchange getHistoricalRates(String date, String appId);
}
