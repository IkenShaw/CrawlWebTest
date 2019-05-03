package org.iken.main;

import org.iken.main.controller.RequestAndResponseTool;
import org.iken.main.filter.Links;
import org.iken.main.filter.LinksFilter;
import org.iken.main.page.Page;
import org.iken.main.page.PageParseTool;
import org.iken.main.util.FileTool;
import org.jsoup.select.Elements;

import java.util.Set;

public class MyCrawler {
    private void initCrawlerWithSeeds(String[] seeds){
        for (int i = 0; i < seeds.length; i++){
            Links.addUnVisitedUrlList(seeds[i]);
        }
    }

    public void crawling(String[] seeds){
        initCrawlerWithSeeds(seeds);
        LinksFilter filter = new LinksFilter() {
            public boolean accept(String url) {
                if (url.startsWith("http://www.baidu.com")){
                    return true;
                }
                return false;
            }
        };
        String visitUrl = null;
        while (!Links.unVisitedUrlListIsEmpty() && Links.getVisitedUrlNum() <= 1000) {
            visitUrl = (String) Links.removeFirstUnVisitedUrlList();
            if (visitUrl == null) {
                continue;
            }
            Page page = RequestAndResponseTool.sendRequestAndGetResponse(visitUrl);
            Elements elements = PageParseTool.select(page, "a");
            if (!elements.isEmpty()) {
                System.out.println("下面将打印所有a标签");
                System.out.println(elements);
            }
            FileTool.saveToLocal(page);
            Links.addVisitedUrlSet(visitUrl);
            Set<String> links = PageParseTool.getLinks(page, "img");
            for (String link:links) {
                Links.addUnVisitedUrlList(link);
                System.out.println("新增爬取路径：" + link);
            }
        }
    }

    public static void main(String[] args) {
        MyCrawler myCrawler = new MyCrawler();
        myCrawler.crawling(new String[]{"http://www.baidu.com"});
    }
}
