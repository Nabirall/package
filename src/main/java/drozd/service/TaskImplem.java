package drozd.service;

import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface TaskImplem {

    ResponseEntity<Map> getGif(String tag);


}


