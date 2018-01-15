package com.lays.indexer;

import java.io.Serializable;
import java.util.List;

public class Term implements Serializable {
    SkipLinkedList<IndexRecord> indexRecords = new SkipLinkedList<>();
//    SkipLinkedList<IndexDataMap> linkedIndexDatas = new SkipLinkedList<>();
    public String keyword;

    public Term(String keyword) {
        this.keyword = keyword.toLowerCase();
    }

    public List<IndexRecord> getIndexRecords() {
        return indexRecords;
    }

//    public void setIndexDatas(List<IndexDataMap> indexRecords) {
//        this.indexRecords = indexRecords;
//    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
