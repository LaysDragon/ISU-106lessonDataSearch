package com.lays.indexer;

import java.io.Serializable;

public class TokenPosition implements Serializable {
    int index;
    int startPosition;
    int endPosition;
    String keywords;

    public int getIndex() {
        return index;
    }

    public int getStartPosition() {
        return startPosition;
    }

    public int getEndPosition() {
        return endPosition;
    }

    public String getKeywords() {
        return keywords;
    }

    public TokenPosition(int index, int startPosition, int endPosition, String keywords) {
        this.index = index;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.keywords = keywords;
    }
}
