package scot.shaun.cvgen.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHttpRequest;
import org.apache.http.util.EntityUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import scot.shaun.cvgen.model.CvModel;
import scot.shaun.cvgen.model.Theme;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
public class CvService
{
    public CvModel getModel() throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("https://raw.githubusercontent.com/shauncampbell/curriculum-vitae/master/shauncampbell.json");
        CloseableHttpResponse response1 = httpclient.execute(httpGet);

        try {
            System.out.println(response1.getStatusLine());
            HttpEntity entity1 = response1.getEntity();
            String e = EntityUtils.toString(entity1);

            ObjectMapper mapper = new ObjectMapper();
            CvModel cvModel = mapper.readValue(e, CvModel.class);
            return cvModel;
        } finally {
            response1.close();
        }
    }

    public String generateStylesheet() throws IOException {
        CvModel model = getModel();
        InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("style.css");
        try {
            String stylesheet = IOUtils.toString(resourceAsStream, Charset.defaultCharset());

            for (Map.Entry<String, String> entry : model.getTheme().getColours().entrySet())
            {
                stylesheet = stylesheet.replace("%" + entry.getKey() + "%", entry.getValue());
            }

            stylesheet = stylesheet.replace("%profilePicture%", model.getTheme().getProfilePicture());
            return stylesheet;


        } catch (IOException e) {
            return "";
        }
    }


}
