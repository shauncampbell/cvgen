package scot.shaun.cvgen.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yahoo.platform.yui.compressor.CssCompressor;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.StringBuilderWriter;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;
import scot.shaun.cvgen.model.CvModel;
import scot.shaun.cvgen.util.CVPdfWriter;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Map;

@Service
public class CvService
{
    private CvModel model = null;
    private String stylesheet = null;
    private static final Object OBJECT = new Object();

    public CvModel getModel() throws IOException {
        if (null != model) {
            return model;
        }

        refreshData();

        return model;
    }

    public void refreshData() throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("https://raw.githubusercontent.com/shauncampbell/curriculum-vitae/master/shauncampbell.json");
        CloseableHttpResponse response1 = httpclient.execute(httpGet);

        try {
            System.out.println(response1.getStatusLine());
            HttpEntity entity1 = response1.getEntity();
            String e = EntityUtils.toString(entity1);

            ObjectMapper mapper = new ObjectMapper();
            this.model = mapper.readValue(e, CvModel.class);

        } finally {
            response1.close();
        }
    }

    public String generateStylesheet() throws IOException {
        if (null != stylesheet)
        {
            return stylesheet;
        }

        synchronized (OBJECT) {
            if (null != stylesheet)
            {
                return stylesheet;
            }
            CvModel model = getModel();
            InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("style.css");
            try {
                if (null == resourceAsStream) {
                    throw new RuntimeException("No reference stylesheet");
                }
                long perf = System.currentTimeMillis();
                String stylesheet = IOUtils.toString(resourceAsStream, Charset.defaultCharset());
                for (Map.Entry<String, String> entry : model.getTheme().getColours().entrySet()) {
                    stylesheet = stylesheet.replace("%" + entry.getKey() + "%", entry.getValue());
                }
                stylesheet = stylesheet.replace("%profilePicture%", model.getTheme().getProfilePicture());
                System.out.printf("Stylesheet generated in %d ms\n", System.currentTimeMillis() - perf);
                this.stylesheet = minifyCss(stylesheet);
                return this.stylesheet;
            } catch (IOException e) {
                return "";
            }
        }
    }

    private String minifyCss(String input) throws IOException {
        long start = System.currentTimeMillis();
        CssCompressor compressor = new CssCompressor(new StringReader(input));
//        compressor.
        StringBuilderWriter writer = new StringBuilderWriter();
        compressor.compress(writer, 4096);
        System.out.printf("Stylesheet minified in %d ms\n", System.currentTimeMillis() - start);
                return writer.toString();
    }

    public byte[] getPdfVersion() throws IOException {
        return new CVPdfWriter(getModel()).getPdf();
    }



}
