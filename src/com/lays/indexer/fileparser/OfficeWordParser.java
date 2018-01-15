package com.lays.indexer.fileparser;

import javax.servlet.http.Part;
import java.io.IOException;
import  org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

public class OfficeWordParser extends FileParser {
    public OfficeWordParser() {
        super(new String[]{"docx"});
    }

    @Override
    public String Parse(Part filepart) throws IOException {
        return new XWPFWordExtractor(new XWPFDocument(filepart.getInputStream())).getText();
    }
}
