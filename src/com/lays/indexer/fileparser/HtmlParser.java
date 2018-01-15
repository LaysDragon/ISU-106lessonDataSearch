package com.lays.indexer.fileparser;

import com.lays.indexer.Document;
import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.nio.charset.Charset;

public class HtmlParser extends FileParser {
    public HtmlParser() {
        super(new String[]{"html","htm"});
    }

    @Override
    public String Parse(Part filepart) throws IOException {
        String content = IOUtils.toString(filepart.getInputStream(), Charset.forName("utf-8"));
        content = Jsoup.parse(content).text();
        return content;
    }

    @Override
    public void handleResponse(Document doc, HttpServletResponse response) {
        response.setContentType("text/html");
        response.setHeader("Content-Disposition", "filename=\""+doc.getName()+"\"");
//        ServletOutputStream outputStream = null;

        try (ServletOutputStream outputStream = response.getOutputStream()){
            outputStream.write(doc.getBinaryContent());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
