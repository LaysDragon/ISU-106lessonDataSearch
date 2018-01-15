package com.lays.indexer;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import edu.uci.ics.crawler4j.url.WebURL;
import org.apache.commons.lang.exception.ExceptionUtils;

import java.util.regex.Pattern;

public class WebPageCrawlerController {
    private static WebPageCrawlerController ourInstance = new WebPageCrawlerController();
    private CrawlController controller;

    public static WebPageCrawlerController getInstance() {
        return ourInstance;
    }

    private WebPageCrawlerController() {
    }

    public int pagesCount = 0;
    public String log = "";
    public String target_url = "";
//    public boolean processing = false;

    public void start(String url) {
        try {
            if(!isFinished())return;
            target_url = url;
            String crawlStorageFolder = "/data/crawl/root";
            int numberOfCrawlers = 10;

            CrawlConfig config = new CrawlConfig();
            config.setCrawlStorageFolder(crawlStorageFolder);
            config.setMaxDepthOfCrawling(5);
        /*
         * Instantiate the controller for this crawl.
         */
            PageFetcher pageFetcher = new PageFetcher(config);
            RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
            RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
            controller = new CrawlController(config, pageFetcher, robotstxtServer);

        /*
         * For each crawl, you need to add some seed urls. These are the first
         * URLs that are fetched and then the crawler starts following links
         * which are found in these pages
         */
            controller.addSeed(url);
//            CrawlController.WebCrawlerFactory webCrawlerFactory = WebPageCrawler::new;
            controller.startNonBlocking(WebPageCrawler::new,numberOfCrawlers);
//            controller.startNonBlocking(WebPageCrawler.class, numberOfCrawlers);
            pagesCount=0;
            log = "";
//            processing = true;

        } catch (Exception e) {
//            processing = false;
            log = ExceptionUtils.getStackTrace(e) +log;
            e.printStackTrace();
        }
    }

    public void stop() throws Exception {
        log = "正在停止中...\n" +log;
        controller.shutdown();
        controller.waitUntilFinish();
        log = "已停止收集資料\n" +log;
//        processing = false;
    }

    public boolean isFinished(){
        if (controller == null) {
            return true;
        }
        return controller.isFinished();
    }

    public class WebPageCrawler extends WebCrawler {
        public WebPageCrawler() {
            super();
        }

        @Override
        public boolean shouldVisit(Page referringPage, WebURL url) {
            String href = url.getURL().toLowerCase();
            return !Pattern.compile(".*(\\.(css|js|gif|jpg|png|mp3|mp3|zip|gz|ttf))$").matcher(href).matches();
        }

        @Override
        public void visit(Page page) {
            pagesCount++;
            log = "訪問網址:" + page.getWebURL()+"\n" +log;
//            System.out.println("訪問網址:" + page.getWebURL());
            try {
                IndexManager.getInstance().addDocument( Document.getDocFromWebPage(page));
            } catch (Exception e) {
                log =  ExceptionUtils.getStackTrace(e) + "\n" +log;
                e.printStackTrace();
            }
        }
    }
}