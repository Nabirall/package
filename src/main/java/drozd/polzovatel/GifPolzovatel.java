package drozd.polzovatel;

import org.springframework.http.ResponseEntity;


import java.util.Map;

public interface GifPolzovatel {

    ResponseEntity<Map> getRandomGif(String apiKey, String tag);
}
