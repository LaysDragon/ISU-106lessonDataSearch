package com.lays.bean;

import com.lays.indexer.WebPageCrawlerController;

public class CrawlerBean {

    public String getLog(){
        return WebPageCrawlerController.getInstance().log.replaceAll("\n","<br>");
    }

    public int getPageCount(){
        return WebPageCrawlerController.getInstance().pagesCount;
    }

    public String getTargetUrl(){
        return WebPageCrawlerController.getInstance().target_url;
    }

    public boolean isFinished(){
        return WebPageCrawlerController.getInstance().isFinished();
    }

    public void start(String url){
        try {
            WebPageCrawlerController.getInstance().start(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void stop(){
        try {
            WebPageCrawlerController.getInstance().stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
