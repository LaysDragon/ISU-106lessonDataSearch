package com.lays.bean;

import com.lays.indexer.IndexManager;
import com.lays.indexer.Term;

import java.util.List;
import java.util.TreeMap;

public class IndexsBean {

    public IndexsBean() {
    }


    public TreeMap<String, Term> getIndexRecords() {
        return IndexManager.getInstance().IndexDataMap;
    }

    public TreeMap<String, List<Term>> getGram2IndexDatas() {
        return IndexManager.getInstance().Gram2IndexDataMap;
    }

    public TreeMap<String, Term> getBiwordIndexDatas() {
        return IndexManager.getInstance().BiwordIndexDataMap;
    }

    public TreeMap<String, Term> getPermutermIndex(){
        return IndexManager.getInstance().PermutermIndex;
    }
}
