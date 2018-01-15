package com.lays.indexer.fileparser;

import com.lays.indexer.Document;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

abstract public class FileParser extends Parser {
    public FileParser(String[] acceptTypes) {
        super(acceptTypes);
    }

    @Override
    public void handleResponse(Document doc, HttpServletResponse response) {
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "filename=\""+doc.getName()+"\"");
        ServletOutputStream outputStream = null;

        try {
            outputStream = response.getOutputStream();
            outputStream.write(doc.getBinaryContent());
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
