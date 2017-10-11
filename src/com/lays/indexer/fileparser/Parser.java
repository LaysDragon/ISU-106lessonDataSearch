package com.lays.indexer.fileparser;

import com.sun.org.apache.xpath.internal.operations.Bool;

import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

abstract public class Parser {
    List<String> acceptTypes = new ArrayList<>();

    public Parser(String[] acceptTypes) {
        this.acceptTypes.addAll(Arrays.asList(acceptTypes));
    }

    public abstract String Parse(Part filepart) throws IOException;

    public static Parser[] RegisteredParsers = {
            new TextParser()
    };
    public static String TryParse(Part filepart) throws IOException ,Exception{
        String fileName = filepart.getSubmittedFileName();
        String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1);
        for (Parser parser : RegisteredParsers) {
            if (parser.acceptTypes.contains(fileExt)) {
                return parser.Parse(filepart);
            }
        }
        throw new Exception("不支持的檔案類型:"+fileExt);
    }

}
