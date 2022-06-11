package drozd.service;

import drozd.client.GifClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class GifServiceImpl implements GifService {
    private final GifClient gifClient;
    @Value("${giphy.api.key}")
    private String apiKey;
    @Value("${giphy.rich}")
    private String richTag;
    @Value("${giphy.broke}")
    private String brokeTag;
    @Value("${giphy.error}")
    private String errorTag;

    public GifServiceImpl(GifClient gifClient) {
        this.gifClient = gifClient;
    }

    @Override
    public ResponseEntity<Map> getGif(int trend) {
        String gifTag = this.errorTag;
        switch (trend) {
            case 1:
                gifTag = this.richTag;
                break;
            case -1:
                gifTag = this.brokeTag;
                break;
        }

        ResponseEntity<Map> result = gifClient.getRandomGif(this.apiKey, gifTag);
        result.getBody().put("compareResult", gifTag);

        return result;
    }
}