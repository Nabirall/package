package drozd.controller;

import drozd.service.TaskImplem;
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

    private ChangeService changeService;
    private TaskImplem taskImplem;
    @Value("${giphy.rich}")
    private String richTag;
    @Value("${giphy.broke}")
    private String brokeTag;
    @Value("${giphy.error}")
    private String errorTag;
    private String gifTag;

    @Autowired
    public MainController(
            ChangeService changeService,
            TaskImplem taskImplem
    ) {
        this.changeService = changeService;
        this.taskImplem = taskImplem;
    }


    @GetMapping("/getcodes")
    public List<String> getCharCodes() {
        return changeService.getCharCodes();
    }


    @GetMapping("/getgif/{code}")
    public ResponseEntity<Map> getGif(@PathVariable String code) {
        ResponseEntity<Map> result = null;

        int gifKey = -101;
        String gifTag = this.errorTag;
        if (code != null) {
            gifKey = changeService.getKeyForTag(code);
        }
        switch (gifKey) {
            case 1:
                gifTag = this.richTag;
                break;
            case -1:
                gifTag = this.brokeTag;
                break;
        }
        result = taskImplem.getGif(gifTag);
        return result;
    }

}