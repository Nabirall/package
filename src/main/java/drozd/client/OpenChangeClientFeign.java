package drozd.client;

import drozd.model.Exchange;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(name = "OERClient", url = "${openexchangerates.url.general}")
public interface OpenChangeClientFeign extends OpenChangeClient {

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
