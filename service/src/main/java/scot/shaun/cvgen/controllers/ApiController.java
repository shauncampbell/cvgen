package scot.shaun.cvgen.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import scot.shaun.cvgen.model.CurriculumVitae;
import scot.shaun.cvgen.services.CvService;
import scot.shaun.cvgen.util.PdfWriterException;

import java.io.IOException;
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
    public ResponseEntity<CurriculumVitae> getContent()
    {
        try {
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(service.getModel().getContent());
        } catch (IOException e) {
            return ResponseEntity.badRequest()
                                 .body(new CurriculumVitae());
        }
    }

    @RequestMapping(path = "/pdf")
    public ResponseEntity<byte[]> getPdf()
    {
        try {
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).body(service.getPdfVersion());
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(e.toString().getBytes());
        }
    }

    @RequestMapping(path = "/refresh", method = RequestMethod.POST)
    public ResponseEntity<Void> refreshFromGitCommit(@RequestBody Map<String, Object> event)
    {
        System.out.println(event != null ? event.toString() : "No event body");

        try {
            service.refreshData();
        } catch (IOException e) {
            e.printStackTrace();
            ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(PdfWriterException.class)
    public ResponseEntity<String> produceErrorMessage(PdfWriterException exception)
    {
        return ResponseEntity.status(500).body(exception.getMessage());
    }
}
