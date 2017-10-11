package com.lays.servlet;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.Collection;

@MultipartConfig
public class docs_upload extends javax.servlet.http.HttpServlet {
    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        Collection<Part> fileparts = request.getParts();
        for (Part part : fileparts) {
            response.setContentType("text/html");
            response.getWriter().write(part.getSubmittedFileName()+"<br>");
            part.getInputStream();
        }
    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {

    }
}
