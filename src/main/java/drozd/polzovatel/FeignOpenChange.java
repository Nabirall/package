package drozd.polzovatel;

import drozd.shablon.Exchange;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@org.springframework.cloud.openfeign.FeignClient(name = "OERClient", url = "${openexchangerates.url.general}")
public interface FeignOpenChange extends OpenChangePolzovatel {

    @Override
    @GetMapping("/latest.json")
    Exchange getLatestRates(
            @RequestParam("app_id") String appId
    );

    @Override
    @GetMapping("/historical/{date}.json")
    Exchange getHistoricalRates(
            @PathVariable String date,
            @RequestParam("app_id") String appId
    );
}
