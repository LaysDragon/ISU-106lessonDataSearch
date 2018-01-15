package com.lays.indexer.fileparser;

import com.lays.indexer.Document;
import com.sun.org.apache.xpath.internal.operations.Bool;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

abstract public class Parser {
    List<String> acceptTypes = new ArrayList<>();

    public Parser(String[] acceptTypes) {
        this.acceptTypes.addAll(Arrays.asList(acceptTypes));
    }

    public abstract String Parse(Part filepart) throws IOException;

    public abstract void handleResponse(Document doc,HttpServletResponse response);


}
