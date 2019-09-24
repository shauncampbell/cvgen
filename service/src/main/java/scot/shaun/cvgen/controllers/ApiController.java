package scot.shaun.cvgen.controllers;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.yaml.snakeyaml.reader.StreamReader;
import scot.shaun.cvgen.services.CvService;

import javax.print.attribute.standard.Media;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(path = "/api/v1/")
public class ApiController
{
    @Autowired
    private CvService service;


    @RequestMapping(path = "/stylesheet")
    public ResponseEntity<String> getStylesheet() throws IOException {
        return ResponseEntity.ok().contentType(MediaType.parseMediaType("text/css")).body(service.generateStylesheet());
    }

    @RequestMapping(path = "/content")
    public ResponseEntity<Map<String, Object>> getContent()
    {
        try {
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(service.getModel().getContent());
        } catch (IOException e) {
            return ResponseEntity.badRequest()
                                 .body(new HashMap<>());
        }
    }
}
