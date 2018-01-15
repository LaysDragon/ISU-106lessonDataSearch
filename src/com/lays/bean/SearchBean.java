package com.lays.bean;

import com.lays.indexer.IndexRecord;
import com.lays.indexer.IndexManager;

import java.util.ArrayList;
import java.util.List;

public class SearchBean {

    List<IndexRecord> result = new ArrayList<>();
    private boolean wildModeSwitch = true;
    private boolean skipMode = true;
    private String correctionQuerry = "";

    public void setQuery(String query) {
        this.correctionQuerry = "";
        this.result = IndexManager.getInstance().search(query, wildModeSwitch, skipMode);
        if(result.size()==0)
            if(query.trim().length()>0)
                this.correctionQuerry = IndexManager.getInstance().queryCorrection(query);
    }

    public void setWildCardMode(boolean modeSwitch){
        this.wildModeSwitch = modeSwitch;
    }

    public void setSkipMode(boolean skipMode) {
        this.skipMode = skipMode;
    }

    public List<IndexRecord> getResult() {
        return this.result;
    }

    public int getAndOpCounter() {
        return IndexManager.getInstance().andOpLoopCounter;
    }

    public String getCorrectionQuerry() {
        return correctionQuerry;
    }
}
