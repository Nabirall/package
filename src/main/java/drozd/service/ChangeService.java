package drozd.service;

import drozd.model.Exchange;

import java.util.List;
import java.util.Map;


public interface ChangeService {

    List<String> getCharCodes();

    int getTrendForTicker(String charCode);

    List<Exchange> refreshRates();

    String getBase();

    long getTimestamp();

    Map<String, Double> getRates();


}
