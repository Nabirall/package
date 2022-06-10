package drozd.polzovatel;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;


@org.springframework.cloud.openfeign.FeignClient(name = "giphyClient", url = "${giphy.url.general}")
public interface FeignGiphyGif extends GifPolzovatel {

    @Override
    @GetMapping("/random")
    ResponseEntity<Map> getRandomGif(
            @RequestParam("api_key") String apiKey,
            @RequestParam("tag") String tag
    );

}