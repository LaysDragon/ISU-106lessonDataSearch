package com.lays.indexer.fileparser;

import org.apache.commons.io.IOUtils;

import javax.servlet.http.Part;
import java.io.IOException;
import java.nio.charset.Charset;

public class TextParser extends Parser {
    public TextParser() {
        super(new String[]{"txt","html"});
    }

    @Override
    public String Parse(Part filepart) throws IOException {
        return IOUtils.toString(filepart.getInputStream(), Charset.forName("utf-8"));
    }
}
