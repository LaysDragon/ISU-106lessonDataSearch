package com.lays.indexer.fileparser;

import javax.servlet.http.Part;
import java.io.IOException;

public class TextParser extends Parser {
    public TextParser() {
        super(new String[]{"txt","html"});
    }

    @Override
    public String Parse(Part filepart) throws IOException {
        return filepart.getInputStream().toString();
    }
}
