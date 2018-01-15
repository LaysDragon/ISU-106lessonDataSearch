package com.lays.indexer;

import com.lays.indexer.fileparser.ParserManager;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.parser.ParseData;
import edu.uci.ics.crawler4j.parser.TextParseData;
import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.Serializable;
import java.util.function.BiConsumer;

public class Document implements Serializable {
    //String path="";
    final String type;//="";
    final String name;//="";
    final String textContent;//="";
    final byte[] binaryContent;
    final int id;
    final BiConsumer<Document,HttpServletResponse> responseHandler;

    public Document(int id, String type, String name, String textContent, byte[] binaryContent, BiConsumer<Document, HttpServletResponse> responseHandler) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.textContent = textContent;
        this.binaryContent = binaryContent;
        this.responseHandler = responseHandler;
    }
    public static int lastDocId = 0;
    public static synchronized Document getDocFromPart(Part filepart)  {
            String fileName = filepart.getSubmittedFileName();
            String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1);
            try {
                return new Document(lastDocId++, fileExt, fileName, ParserManager.TryParseFile(filepart), IOUtils.toByteArray(filepart.getInputStream()), ParserManager.getResponserHandler(fileExt));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
    }

    public static synchronized Document getDocFromWebPage(Page page) throws Exception {
        ParseData parseData = page.getParseData();
        String content = "";
        if(parseData instanceof HtmlParseData) {
            content = ((HtmlParseData) parseData).getHtml();
            content = Jsoup.parse(content).text();
        }else if(parseData instanceof TextParseData) {
            content = ((TextParseData) parseData).getTextContent();
            throw new Exception("不接受純Text類型的資料!!");
        }else
            throw new Exception("不接受Binary類型的資料!!");
        return new Document(lastDocId++, "url", page.getWebURL().toString(), content, null, (document, response) -> {
                try {
                    response.sendRedirect(document.name);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getTextContent() {
        return textContent;
    }

    public byte[] getBinaryContent() {
        return binaryContent;
    }

    public int getId() {
        return id;
    }

    public void handleRequest(HttpServletResponse response){
        responseHandler.accept(this,response);
    }

    public static int getLastDocId() {
        return lastDocId;
    }

    public static void setLastDocId(int lastDocId) {
        Document.lastDocId = lastDocId;
    }
}
