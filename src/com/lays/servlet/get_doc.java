package com.lays.servlet;

import com.lays.indexer.Document;
import com.lays.indexer.IndexManager;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "get_doc")
public class get_doc extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        Document doc = IndexManager.getInstance().getDoc(Integer.parseInt(id));
        if (doc == null) {
            response.setCharacterEncoding("big5");
            response.getWriter().write("查無此文件");
            return;
        }
        doc.handleRequest(response);
//
//        switch (document.getType()){
//            case "url":
//                break;
//            default :
//                response.setContentType("application/octet-stream");
//                response.setHeader("Content-Disposition", "filename=\""+document.getName()+"\"");
//                ServletOutputStream outputStream = response.getOutputStream();
//                outputStream.write(document.getBinaryContent());
//                outputStream.close();
//                break;
//        }

    }
}
