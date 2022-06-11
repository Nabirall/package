package drozd.service;

import drozd.client.OpenChangeClient;
import drozd.model.Exchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ChangeServiceImpl implements ChangeService {
    private final OpenChangeClient openChangeClient;
    private final SimpleDateFormat dateFormat;
    private final SimpleDateFormat timeFormat;
    @Value("${openexchangerates.app.id}")
    private String appId;
    @Value("${openexchangerates.base}")
    private String base;

    @Autowired
    public ChangeServiceImpl(
            OpenChangeClient openChangeClient,
            @Qualifier("date_bean") SimpleDateFormat dateFormat,
            @Qualifier("time_bean") SimpleDateFormat timeFormat
    ) {
        System.out.println("ChangeServiceImpl");
        this.openChangeClient = openChangeClient;
        this.dateFormat = dateFormat;
        this.timeFormat = timeFormat;
    }

    @Override
    public List<String> getCharCodes() {
        List<String> result = null;

        if (this.getRates() != null) {
            result = new ArrayList<>(this.getRates().keySet());
        }

        return result;
    }

    @Override
    public int getTrendForTicker(String ticker) {
        var newRates = this.refreshRates();

        Double prevRatio = this.getRatio(newRates.get(0), ticker);
        Double currentRatio = this.getRatio(newRates.get(1), ticker);

        return prevRatio != null && currentRatio != null
                ? Double.compare(currentRatio, prevRatio)
                : -101;
    }

    @Override
    public List<Exchange> refreshRates() {
        long currentTime = System.currentTimeMillis();
        return Arrays.asList(this.refreshPrevRates(currentTime), this.refreshCurrentRates(currentTime));
    }

    @Override
    public Map<String, Double> getRates() {
        System.out.println("getRates");
        return this.refreshRates().get(0).getRates();
    }

    @Override
    public String getBase() {
        return null;
    }

    @Override
    public long getTimestamp() {
        return 0;
    }

    private Exchange refreshCurrentRates(long time) {
        String date = timeFormat.format(this.getTimestamp() * 1000);
        if (!date.equals(timeFormat.format(time))) {
            return openChangeClient.getLatestRates(this.appId);
        }

        return null;
    }

    private Exchange refreshPrevRates(long time) {
        Calendar prevCalendar = Calendar.getInstance();
        prevCalendar.setTimeInMillis(time);

        String currentDate = dateFormat.format(prevCalendar.getTime());
        prevCalendar.add(Calendar.DAY_OF_YEAR, -1);
        String newPrevDate = dateFormat.format(prevCalendar.getTime());

        String date = dateFormat.format(this.getTimestamp() * 1000);
        if (!date.equals(newPrevDate) && !date.equals(currentDate)) {
            return openChangeClient.getHistoricalRates(newPrevDate, appId);
        }

        return null;
    }

    private Double getRatio(Exchange exchange, String charCode) {
        Double result = null;
        Double targetRate = null;
        Double appBaseRate = null;
        Double defaultBaseRate = null;
        Map<String, Double> rates;

        if (exchange != null && exchange.getRates() != null) {
            rates = exchange.getRates();
            targetRate = rates.get(charCode);
            appBaseRate = rates.get(this.base);
            defaultBaseRate = rates.get(exchange.getBase());
        }

        if (targetRate != null && appBaseRate != null && defaultBaseRate != null) {
            result = new BigDecimal((defaultBaseRate / appBaseRate) * targetRate)
                    .setScale(4, RoundingMode.UP)
                    .doubleValue();
        }

        return result;
    }
}

