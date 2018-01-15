package com.lays.indexer.fileparser;

import com.lays.indexer.Document;
import org.apache.pdfbox.io.RandomAccess;
import org.apache.pdfbox.io.RandomAccessRead;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;

public class PDFParser extends FileParser {
    public PDFParser() {
        super(new String[]{"pdf"});
    }

    @Override
    public String Parse(Part filepart) throws IOException {
        PDDocument document = PDDocument.load(filepart.getInputStream());
        PDFTextStripper pdfTextStripper = new PDFTextStripper();
        pdfTextStripper.setStartPage(1);
        pdfTextStripper.setEndPage( document.getNumberOfPages());
        String text = pdfTextStripper.getText(document);
        document.close();

        return text;
    }

    @Override
    public void handleResponse(Document doc, HttpServletResponse response) {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "filename=\""+doc.getName()+"\"");
//        ServletOutputStream outputStream = null;

        try (ServletOutputStream outputStream = response.getOutputStream()){
            outputStream.write(doc.getBinaryContent());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
