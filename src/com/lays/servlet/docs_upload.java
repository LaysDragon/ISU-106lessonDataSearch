package com.lays.servlet;

import com.lays.indexer.IndexManager;
import org.apache.commons.io.IOUtils;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Part;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collection;

@MultipartConfig
public class docs_upload extends javax.servlet.http.HttpServlet {
    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        Collection<Part> fileparts = request.getParts();
        for (Part part : fileparts) {
            response.setContentType("text/html");
            response.getWriter().write(part.getSubmittedFileName()+"<br>");
//            String string = IOUtils.toString(part.getInputStream(), Charset.forName("utf-8"));
//            response.getWriter().write(string +"<br>");

        }
        try {
            IndexManager.getInstance().AddFileParts(fileparts);
        } catch (Exception e) {
            e.printStackTrace();
        }
        response.sendRedirect("/");
    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {

    }
}
