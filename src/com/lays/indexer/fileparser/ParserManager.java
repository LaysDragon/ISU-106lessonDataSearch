package com.lays.indexer.fileparser;

import com.lays.indexer.Document;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.function.BiConsumer;

public class ParserManager {
    public static Parser[] RegisteredParsers = {
            new TextParser(),
            new PDFParser(),
            new OfficeWordParser(),
            new HtmlParser()
    };

    public static String TryParseFile(Part filepart) throws IOException,Exception{
        String fileName = filepart.getSubmittedFileName();
        String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1);
        for (Parser parser : RegisteredParsers) {
            if (parser.acceptTypes.contains(fileExt.toLowerCase())) {
                return parser.Parse(filepart);
            }
        }
        throw new Exception("不支持的檔案類型:"+fileExt);
    }


    public static BiConsumer<Document,HttpServletResponse> getResponserHandler(String type){
        for (Parser parser : RegisteredParsers) {
            if (parser.acceptTypes.contains(type)) {
                return parser::handleResponse;
            }
        }
        return null;
    }
}
