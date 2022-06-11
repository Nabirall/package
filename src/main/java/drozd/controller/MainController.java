package drozd.controller;

import drozd.service.GifService;
import drozd.service.ChangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/gg")
public class MainController {
    private final GifService gifService;
    private final ChangeService changeService;

    @Value("${giphy.rich}")
    private String richTag;

    @Value("${giphy.broke}")
    private String brokeTag;

    @Value("${giphy.error}")
    private String errorTag;

    @Autowired
    public MainController(ChangeService changeService, GifService gifService) {
        this.changeService = changeService;
        this.gifService = gifService;
    }

    @GetMapping("/codes")
    public List<String> getCharCodes() {
        return changeService.getCharCodes();
    }

    @GetMapping("/gif/{ticker}")
    public ResponseEntity<Map> getGif(@PathVariable String ticker) {
        int trend = changeService.getTrendForTicker(ticker);
        return gifService.getGif(trend);
    }
}