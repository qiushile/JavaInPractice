package com.kiscod.http;

import com.goubanjia.ip.util.CrawlUtil;

/**
 * Created by lance on 2017/7/18.
 */
public class Proxy {

    public static void main(String[] args) {
        CrawlUtil crawlUtil = CrawlUtil.getInstance();
        crawlUtil.setOrder("88888888888");
        crawlUtil.setDynamic(true);

        try {
            String html = crawlUtil.proxyGetFromUrl("http://1212.ip138.com/ic.asp", true);
            System.out.println(html);
        } catch (Exception var3) {
            var3.printStackTrace();
        }

    }
}
