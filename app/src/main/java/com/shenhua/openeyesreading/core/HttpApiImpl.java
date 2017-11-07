package com.shenhua.openeyesreading.core;

import android.net.Uri;

import com.shenhua.comlib.AppUtils;
import com.shenhua.comlib.QueryString;
import com.shenhua.openeyesreading.bean.ArticleData;
import com.shenhua.openeyesreading.bean.IHistoryBean;
import com.shenhua.openeyesreading.bean.IHistoryClickRank;
import com.shenhua.openeyesreading.bean.IHistoryDailyPicks;
import com.shenhua.openeyesreading.bean.IHistoryHistoryNews;
import com.shenhua.openeyesreading.bean.IHistoryOldPhoto;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 业务实现者
 * Created by shenhua on 2016/7/28.
 */
public class HttpApiImpl implements HttpApi {

    public static final int SUCCESS = 0;
    public static final int FAILED = -1;
    private static HttpApiImpl sHttpApiImpl;
    private static AppUtils.HttpUtils sHttpManager = new AppUtils.HttpUtils();
    // 所有异步任务共用一个线程池，避免资源浪费，如若遇到多线程并发任务，需重新指派线程
    private static ExecutorService service;

    private HttpApiImpl() {
    }

    public static HttpApiImpl getInstance() {
        if (sHttpApiImpl == null) {
            sHttpApiImpl = new HttpApiImpl();
        }
        if (service == null) {
            service = Executors.newSingleThreadExecutor();
        }
        return sHttpApiImpl;
    }

    /**
     * 工具方法
     *
     * @param method
     * @return url
     */
    private String buildPostUrl(String method) {
        return Uri.parse(AppUtils.Constants.URL).buildUpon()
                .appendEncodedPath(method)
                .build().toString();
    }

    /**
     * 发射异步任务
     *
     * @param runnable
     */
    private void sendRunnable(Runnable runnable) {
        if (runnable != null) {
            service.submit(runnable);
        }
    }

    @Override
    public void toGetBingImgs(String format, String idx, String n, HttpApiCallback callback) {
        final String url = AppUtils.Constants.URL_IMG_BING;
        final QueryString qs = new QueryString();
        qs.add("format", format);
        qs.add("idx", idx);
        qs.add("n", n);
        sendRunnable(new Runnable() {
            @Override
            public void run() {
                String result = sHttpManager.doGet(url, qs.toString());
                System.out.println("shenhua sout:" + result);
            }
        });
    }

    @Override
    public void toGetArticle(final String url, final HttpApiCallback callback) {
        sendRunnable(new Runnable() {
            @Override
            public void run() {
                try {
                    Document document = Jsoup.connect(url).timeout(10000).ignoreHttpErrors(true).get();
                    Element element = document.getElementsByClass("container").get(0);
                    String title = element.getElementsByClass("articleTitle").text();
                    String auth = element.getElementsByClass("articleAuthorName").text();
                    String content = element.getElementsByClass("articleContent").toString().trim();
                    ArticleData data = new ArticleData();
                    data.setTitle(title);
                    data.setAuth(auth);
                    data.setContent(AppUtils.HtmlString.HTML_TITLE + title +
                            AppUtils.HtmlString.HTML_TITLE_AUTH + auth +
                            AppUtils.HtmlString.HTML_TITLE_END +
                            content +
                            AppUtils.HtmlString.HTML_END);
                    data.setImgUrl(getRandomImgUrl());
                    callback.obtainMessage(HttpApiImpl.SUCCESS, data).sendToTarget();
                } catch (IOException e) {
                    e.printStackTrace();
                    callback.obtainMessage(HttpApiImpl.FAILED, "数据错误").sendToTarget();
                }
            }

            private String getRandomImgUrl() {
                int random_bg = (int) Math.floor(Math.random() * 99 + 1);
                if (random_bg == 99) {
                    random_bg = 98;
                }
                return AppUtils.Constants.URL_ARTICLE_IMG + random_bg + ".jpg";
            }
        });
    }

    @Override
    public void toGetIHistory(final String url, final HttpApiCallback callback) {
        sendRunnable(new Runnable() {
            @Override
            public void run() {
                try {
                    Document document = Jsoup.connect(url).timeout(10000).ignoreHttpErrors(true).get();
                    if (document != null) {
                        IHistoryBean bean = new IHistoryBean();
                        bean.setDocString(document.toString());
                        callback.obtainMessage(HttpApiImpl.SUCCESS, bean).sendToTarget();
                    } else callback.obtainMessage(HttpApiImpl.FAILED, "获取数据失败").sendToTarget();
                } catch (IOException e) {
                    e.printStackTrace();
                    callback.obtainMessage(HttpApiImpl.FAILED, "数据错误").sendToTarget();
                }
            }
        });
    }

    /**
     * 每日推荐
     *
     * @param str
     */
    public List<IHistoryDailyPicks> takeDailyPick(String str) {
        Document document = Jsoup.parse(str);
        List<IHistoryDailyPicks> dailyPicksList = new ArrayList<>();
        Elements element = document.getElementsByClass("tuijian").get(0)
                .getElementsByClass("box");
        for (Element element2 : element) {
            IHistoryDailyPicks dailyPicks = new IHistoryDailyPicks();
            Element info = element2.getElementsByClass("info").get(0);
            dailyPicks.setTitle(info.getElementsByTag("a").text());// title
            String time = info.getElementsByClass("time").text().trim();
            dailyPicks.setTime(time.substring(0, time.length() - 1));// time
            dailyPicks.setDiscuss(info.getElementsByClass("pinglun").text());// Discuss
            dailyPicks.setDescribe(element2.getElementsByClass("info1").text());// Describe
            dailyPicks.setHref(AppUtils.Constants.URL_ILISHI + info.getElementsByTag("a").attr("href"));// Href
            dailyPicks.setImgHref(AppUtils.Constants.URL_ILISHI + element2.getElementsByTag("img").attr("src"));// imgHref
            dailyPicksList.add(dailyPicks);
        }
        return dailyPicksList;
    }

    /**
     * 点击排行
     *
     * @param str
     */
    public List<IHistoryClickRank> takeClickRank(String str) {
        Document document = Jsoup.parse(str);
        List<IHistoryClickRank> clickRankList = new ArrayList<>();
        Elements elements = document.getElementsByClass("click");
        Elements element = elements.get(0).getElementsByTag("li");
        for (Element element2 : element) {
            IHistoryClickRank clickRank = new IHistoryClickRank();
            clickRank.setTitle(element2.getElementsByTag("a").text());// title
            clickRank.setHref(AppUtils.Constants.URL_ILISHI + element2.getElementsByTag("a").attr("href"));// href
            clickRankList.add(clickRank);
        }
        return clickRankList;
    }

    /**
     * 推荐阅读
     *
     * @param str
     */
    public List<IHistoryOldPhoto> takeProposeRead(String str) {
        Document document = Jsoup.parse(str);
        List<IHistoryOldPhoto> proposeReads = new ArrayList<>();
        Elements elements = document.getElementsByClass("oldpic");
        Elements element = elements.get(0).getElementsByTag("li");
        for (Element element2 : element) {
            IHistoryOldPhoto proposeRead = new IHistoryOldPhoto();
            proposeRead.setTitle(element2.getElementsByTag("img").attr("title"));//title
            proposeRead.setHref(AppUtils.Constants.URL_ILISHI + element2.getElementsByTag("a").attr("href"));//href
            proposeRead.setImgHref(AppUtils.Constants.URL_ILISHI + element2.getElementsByTag("img").attr("src"));//imgHref
            proposeReads.add(proposeRead);
        }
        return proposeReads;
    }

    /**
     * 历史新闻
     *
     * @param str
     */
    public List<IHistoryHistoryNews> takeHistoryNews(String str) {
        Document document = Jsoup.parse(str);
        List<IHistoryHistoryNews> historyNewses = new ArrayList<>();
        Elements elements = document.getElementsByClass("miwen");
        Elements element = elements.get(0).getElementsByTag("ul");
        for (Element element2 : element) {
            IHistoryHistoryNews historyNews = new IHistoryHistoryNews();
            historyNews.setTitle(element2.getElementsByClass("info").get(0).getElementsByTag("a").text());//title
            historyNews.setTime(element2.getElementsByClass("time").get(0).text());//time
            historyNews.setHref(AppUtils.Constants.URL_ILISHI + element2.getElementsByClass("info").get(0).getElementsByTag("a").attr("href"));//href
            historyNews.setImgHref(AppUtils.Constants.URL_ILISHI + element2.getElementsByTag("img").attr("src"));//imgHref
            historyNewses.add(historyNews);
        }
        return historyNewses;
    }

}