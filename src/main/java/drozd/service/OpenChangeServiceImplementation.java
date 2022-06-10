package drozd.service;

import drozd.polzovatel.OpenChangePolzovatel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;


@Service
public class OpenChangeServiceImplementation implements ChangeService {

    private ChangeService prevRates;
    private ChangeService currentRates;

    private OpenChangePolzovatel openChangePolzovatel;
    private SimpleDateFormat dateFormat;
    private SimpleDateFormat timeFormat;
    @Value("${openexchangerates.app.id}")
    private String appId;
    @Value("${openexchangerates.base}")
    private String base;
    @Autowired
    public OpenChangeServiceImplementation(
            OpenChangePolzovatel openChangePolzovatel,
            @Qualifier("date_bean") SimpleDateFormat dateFormat,
            @Qualifier("time_bean") SimpleDateFormat timeFormat
    ) {
        this.openChangePolzovatel = openChangePolzovatel;
        this.dateFormat = dateFormat;
        this.timeFormat = timeFormat;
    }




    @Override
    public List<String> getCharCodes() {
        List<String> result = null;
        if (this.currentRates.getRates() != null) {
            result = new ArrayList<>(this.currentRates.getRates().keySet());
        }
        return result;
    }



    @Override
    public int getKeyForTag(String charCode) {
        this.refreshRates();
        Double prevCoefficient = this.getCoefficient(this.prevRates, charCode);
        Double currentCoefficient = this.getCoefficient(this.currentRates, charCode);
        return prevCoefficient != null && currentCoefficient != null
                ? Double.compare(currentCoefficient, prevCoefficient)
                : -101;
    }

    @Override
    public void refreshRates() {
        long currentTime = System.currentTimeMillis();
        this.refreshCurrentRates(currentTime);
        this.refreshPrevRates(currentTime);
    }

    @Override
    public Map<String, Double> getRates() {
        return null;
    }

    @Override
    public String getBase() {
        return null;
    }

    @Override
    public long getTimestamp() {
        return 0;
    }


    private void refreshCurrentRates(long time) {
        if (
                this.currentRates == null ||
                        !timeFormat.format(Long.valueOf(this.currentRates.getTimestamp()) * 1000)
                                .equals(timeFormat.format(time))
        ) {
            this.currentRates = (ChangeService) openChangePolzovatel.getLatestRates(this.appId);
        }
    }


    private void refreshPrevRates(long time) {
        Calendar prevCalendar = Calendar.getInstance();
        prevCalendar.setTimeInMillis(time);
        String currentDate = dateFormat.format(prevCalendar.getTime());
        prevCalendar.add(Calendar.DAY_OF_YEAR, -1);
        String newPrevDate = dateFormat.format(prevCalendar.getTime());
        if (
                this.prevRates == null
                        || (
                        !dateFormat.format(Long.valueOf(this.prevRates.getTimestamp()) * 1000)
                                .equals(newPrevDate)
                                && !dateFormat.format(Long.valueOf(this.prevRates.getTimestamp()) * 1000)
                                .equals(currentDate)
                )
        ) {
            this.prevRates = (ChangeService) openChangePolzovatel.getHistoricalRates(newPrevDate, appId);
        }
    }


    private Double getCoefficient(ChangeService rates, String charCode) {
        Double result = null;
        Double targetRate = null;
        Double appBaseRate = null;
        Double defaultBaseRate = null;
        Map<String, Double> map = null;
        if (rates != null && rates.getRates() != null) {
            map = rates.getRates();
            targetRate = map.get(charCode);
            appBaseRate = map.get(this.base);
            defaultBaseRate = map.get(rates.getBase());
        }
        if (targetRate != null && appBaseRate != null && defaultBaseRate != null) {
            result = new BigDecimal(
                    (defaultBaseRate / appBaseRate) * targetRate
            )
                    .setScale(4, RoundingMode.UP)
                    .doubleValue();
        }
        return result;
    }

}
