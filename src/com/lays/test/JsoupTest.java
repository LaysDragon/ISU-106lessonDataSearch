package com.lays.test;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class JsoupTest {
    public static void main(String[] args) throws IOException {
        Document parse = Jsoup.parse(FileUtils.readFileToString(FileUtils.getFile("D:\\ProgramCoding\\IdeaProjects\\lesson_DataSearch\\src\\com\\lays\\test\\test.html")));

    }
}
