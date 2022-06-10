package drozd.service;

import java.util.List;
import java.util.Map;

public interface ChangeService {

    List<String> getCharCodes();

    int getKeyForTag(String charCode);

    void refreshRates();

    Map<String, Double> getRates();

    String getBase();


    long getTimestamp();
}