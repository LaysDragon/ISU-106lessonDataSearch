package com.lays.indexer;

import com.lays.indexer.fileparser.Parser;
import org.apache.commons.io.IOUtils;

import javax.servlet.MultipartConfigElement;
import javax.servlet.http.Part;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;

public class IndexManager {
    private static IndexManager ourInstance = new IndexManager();

    public static IndexManager getInstance() {
        return ourInstance;
    }

    private IndexManager() {
    }

    List<Document> Docs  = new ArrayList<>();
    TreeMap<String,Term> IndexDatas = new TreeMap<>();
    public void AddFileParts(Collection<Part> fileparts) throws Exception {
        for (Part part:fileparts) {
            Document doc = Document.getDocFromPart(part);
            List<String> terms = new ArrayList<>();
            for (String term :doc.TextContent.split(" |\\n|\\r")) {
                if (!term.equals("")) {
                    terms.add(term.toLowerCase());
                }
            }
            for (String term :terms) {
                if (this.IndexDatas.containsKey(term)) {
                    List<IndexData> docs = this.IndexDatas.get(term).Docs;
                    Optional<IndexData> dataOptional = docs.stream().filter(indexData -> indexData.Doc.docid == doc.docid).findFirst();
                    if (dataOptional.isPresent()) {
                        dataOptional.get().Frequency++;
                    }else {
                        docs.add(new IndexData(doc,1));
                    }
                }
            }
        }
    }
}

class IndexData{
    public Document Doc;
    public int Frequency;

    public IndexData(Document doc, int frequency) {
        this.Doc = doc;
        Frequency = frequency;
    }
}

class Term{
    List<IndexData> Docs = new ArrayList<>();
    public String Keywords;

    public Term(String keywords) {
        this.Keywords = keywords.toLowerCase();
    }
}