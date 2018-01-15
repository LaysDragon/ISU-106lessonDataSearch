package com.lays.indexer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class IndexRecord implements Serializable {
    public Document document;
    public int frequency;
    public List<TokenPosition> position = new ArrayList<>();
//    public List<Integer> markedToken = new ArrayList<>();
    public List<TokenPosition> markedPosition = new ArrayList<>();

    public IndexRecord(Document document, int frequency) {
        this.document = document;
        this.frequency = frequency;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public List<TokenPosition> getPosition() {
        return position;
    }
}
