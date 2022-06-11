package drozd;

import com.fasterxml.jackson.databind.ObjectMapper;
import drozd.controller.MainController;
import drozd.service.GifServiceImpl;
import drozd.service.ChangeServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringRunner.class)
@WebMvcTest(MainController.class)
public class MainControllerTest {
    @Value("${giphy.rich}")
    private String richTag;
    @Value("${giphy.broke}")
    private String brokeTag;
    @Value("${giphy.error}")
    private String errorTag;

    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ChangeServiceImpl openChangeService;
    @MockBean
    private GifServiceImpl gifService;

    @Test
    public void whenReturnListOfCharCodes() throws Exception {
        List<String> responseList = new ArrayList<>();
        responseList.add("TEST");
        Mockito.when(openChangeService.getCharCodes())
                .thenReturn(responseList);
        mockMvc.perform(get("/gg/codes")
                        .content(mapper.writeValueAsString(responseList))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$[0]").value("TEST"));
    }

    @Test
    public void whenListIsNull() throws Exception {
        Mockito.when(openChangeService.getCharCodes())
                .thenReturn(null);
        mockMvc.perform(get("/gg/codes")
                        .content(mapper.writeValueAsString(null))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$[0]").doesNotExist());
    }

    @Test
    public void whenReturnRichGif() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("compareResult", this.richTag);
        ResponseEntity<Map> responseEntity = new ResponseEntity<>(map, HttpStatus.OK);
        Mockito.when(openChangeService.getTrendForTicker("TESTCODE"))
                .thenReturn(1);
        Mockito.when(gifService.getGif(1))
                .thenReturn(responseEntity);
        mockMvc.perform(get("/gg/gif/TESTCODE")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.compareResult").value(this.richTag));
    }

    @Test
    public void whenReturnBrokeGif() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("compareResult", this.brokeTag);
        ResponseEntity<Map> responseEntity = new ResponseEntity<>(map, HttpStatus.OK);
        Mockito.when(openChangeService.getTrendForTicker("TESTCODE"))
                .thenReturn(-1);
        Mockito.when(gifService.getGif(-1))
                .thenReturn(responseEntity);
        mockMvc.perform(get("/gg/gif/TESTCODE")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.compareResult").value(this.brokeTag));
    }

    @Test
    public void whenReturnErrorGifMinus101() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("compareResult", this.errorTag);
        ResponseEntity<Map> responseEntity = new ResponseEntity<>(map, HttpStatus.OK);
        Mockito.when(openChangeService.getTrendForTicker("TESTCODE"))
                .thenReturn(-101);
        Mockito.when(gifService.getGif(-101))
                .thenReturn(responseEntity);
        mockMvc.perform(get("/gg/gif/TESTCODE")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.compareResult").value(this.errorTag));
    }

    @Test
    public void whenReturnErrorGifAnyOtherKey() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("compareResult", this.errorTag);
        ResponseEntity<Map> responseEntity = new ResponseEntity<>(map, HttpStatus.OK);
        Mockito.when(openChangeService.getTrendForTicker("TESTCODE"))
                .thenReturn(5);
        Mockito.when(gifService.getGif(5))
                .thenReturn(responseEntity);
        mockMvc.perform(get("/gg/gif/TESTCODE")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.compareResult").value(this.errorTag));
    }

}