package drozd.model;

import java.util.Map;


public class Exchange {
    private String base;
    private Map<String, Double> rates;

    public Exchange() {
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getBase() {
        return base;
    }

    public Map<String, Double> getRates() {
        return rates;
    }

    public void setRates(Map<String, Double> rates) {
        this.rates = rates;
    }

    public void setTimestamp(int time) {
    }
}