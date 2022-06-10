package drozd.service;

import drozd.polzovatel.GifPolzovatel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Сервис для работы с Giphy.com
 */
@Service
public class TaskImplementation implements TaskImplem {

    private GifPolzovatel gifPolzovatel;
    @Value("${giphy.api.key}")
    private String apiKey;

    @Autowired
    public TaskImplementation(GifPolzovatel gifPolzovatel) {
        this.gifPolzovatel = gifPolzovatel;
    }


    @Override
    public ResponseEntity<Map> getGif(String tag) {
        ResponseEntity<Map> result = gifPolzovatel.getRandomGif(this.apiKey, tag);
        result.getBody().put("compareResult", tag);


        return result;
    }
}