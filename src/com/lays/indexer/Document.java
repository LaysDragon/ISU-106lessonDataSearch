package com.lays.indexer;

import com.lays.indexer.fileparser.Parser;
import org.apache.commons.io.IOUtils;

import javax.servlet.http.Part;

public class Document {
    //String path="";
    final String type;//="";
    final String name;//="";
    final String TextContent;//="";
    final byte[] BinaryContent;
    final int docid;

    public Document(int docid, String type, String name, String textContent, byte[] binaryContent) {
        this.docid = docid;
        this.type = type;
        this.name = name;
        TextContent = textContent;
        BinaryContent = binaryContent;
    }
    public static int lastDocId = 0;

    public static Document getDocFromPart(Part filepart) throws Exception {
        String fileName = filepart.getSubmittedFileName();
        String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1);
        return new Document(lastDocId++, fileExt,fileName,Parser.TryParse(filepart), IOUtils.toByteArray(filepart.getInputStream()));
    }
}
