package com.shenhua.comlib;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

/**
 * Created by Shenhua on 7/29/2016.
 */
public class AppUtils {

    /**
     * HTTP请求工具类
     */
    public static class HttpUtils {
        private static final int CONNECT_TIMEOUT = 15 * 1000;//建立连接最大等待时间
        private static final int READ_TIMEOUT = 5000;//读取数据最大等待时间
        private static final String CHARSET = "UTF-8";//Http请求内容编码格式

        /**
         * 使用GET访问去访问网络
         *
         * @param getUrl
         * @param param
         * @return 服务器返回的结果
         */
        public String doGet(String getUrl, String param) {
            HttpURLConnection conn = null;
            try {
                URL url = new URL(getUrl + "?" + param);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept-Charset", CHARSET);
                conn.setConnectTimeout(CONNECT_TIMEOUT);
                conn.setReadTimeout(READ_TIMEOUT);
                conn.connect();
                int code = conn.getResponseCode();
                if (code == HttpURLConnection.HTTP_OK) {
                    InputStream is = conn.getInputStream();
                    return getStringFromInputStream(is);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }
            return null;
        }

        /**
         * 根据输入流返回一个字符串
         *
         * @param is
         * @return
         * @throws Exception
         */
        private String getStringFromInputStream(InputStream is) throws Exception {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buff = new byte[1024];
            int len = -1;
            while ((len = is.read(buff)) != -1) {
                baos.write(buff, 0, len);
            }
            is.close();
            String html = baos.toString();
            baos.close();
            return html;
        }
    }

    /**
     * 字符串处理工具类
     */
    public static class StringUtils {


    }

    /**
     * 取一定范围内的随机数 String类型
     *
     * @param min 最小值
     * @param max 最大值
     * @return 随机数
     */
    public static String getRandom(int min, int max) {
        Random random = new Random();
        int r = random.nextInt(max) % (max - min + 1) + min;
        return String.valueOf(r);
    }

    // 生成新的颜色值
    public static int getNewColorByStartEndColor(Context context, float fraction, int startValue, int endValue) {
        return evaluate(fraction, ContextCompat.getColor(context, startValue), ContextCompat.getColor(context, endValue));
    }

    /**
     * 生成新的颜色值
     *
     * @param fraction   颜色取值的级别 (0.0f ~ 1.0f)
     * @param startValue 开始显示的颜色
     * @param endValue   结束显示的颜色
     * @return 返回生成新的颜色值
     */
    public static int evaluate(float fraction, int startValue, int endValue) {
        int startA = (startValue >> 24) & 0xff;
        int startR = (startValue >> 16) & 0xff;
        int startG = (startValue >> 8) & 0xff;
        int startB = startValue & 0xff;
        int endA = (endValue >> 24) & 0xff;
        int endR = (endValue >> 16) & 0xff;
        int endG = (endValue >> 8) & 0xff;
        int endB = endValue & 0xff;
        return ((startA + (int) (fraction * (endA - startA))) << 24) |
                ((startR + (int) (fraction * (endR - startR))) << 16) |
                ((startG + (int) (fraction * (endG - startG))) << 8) |
                ((startB + (int) (fraction * (endB - startB))));
    }

    public static class Constants {
        public static final String URL = "";
        public static final String IMAGE_CACHE_PATH = "Tutu/Cache";
        public static final int MAX_IMG_COUNT = 4050;
        public static final String URL_IMG_INFINITY = "http://img.infinitynewtab.com/wallpaper/%s.jpg";
        public static final String URL_IMG_BING = "http://cn.bing.com/HPImageArchive.aspx";//?format=js&idx=0&n=6
        // http://cn.bing.com/HPImageArchive.aspx?format=js&idx=0&n=12

        public static final String URL_ARTICLE = "http://meiriyiwen.com/index/mobile";
        public static final String URL_ARTICLE_IMG = "http://meiriyiwen.com/images/new_feed/bg_";
        public static final String URL_ARTICLE_RANDOM = "http://meiriyiwen.com/random/iphone";

        public static final String URL_ILISHI = "http://www.ilishi.com/";
        public static final String URL_MEIRIJINGXUAN = "http://www.ilishi.com/meirijingxuan/";//每日精选
        public static final String URL_LISHIJIEMI = "http://www.ilishi.com/jiemi/";//历史揭秘
        public static final String URL_LISHIJAIODIAN = "http://www.ilishi.com/jiaodian/";//历史焦点
        public static final String URL_BAGUALISHI = "http://www.ilishi.com/bagua/";//八卦历史
        public static final String URL_SHEHUIWANXIANG = "http://www.ilishi.com/shehui/";//社会万象
        public static final String URL_LISHIRENWU = "http://www.ilishi.com/renwu/";//历史人物
        public static final String URL_LISHIYINXIANG = "http://www.ilishi.com/yinxiang/";//历史印象
        public static final String URL_ZHANSHIFENGYUN = "http://www.ilishi.com/zhanshi/";//战史风云

        public static final String URL_LISHITUCE = "http://www.ilishi.com/lishitukutu/";//历史图库

        public static final String HOST_NEWS = "http://c.m.163.com/";
        public static final String HOST_SINA_PHOTOS = "http://api.sina.cn/sinago/";

        // 精选列表
        public static final String SINA_PHOTO_CHOICE_ID = "hdpic_toutiao";
        // 趣图列表
        public static final String SINAD_PHOTO_FUN_ID = "hdpic_funny";
        // 美图列表
        public static final String SINAD_PHOTO_PRETTY_ID = "hdpic_pretty";
        // 故事列表
        public static final String SINA_PHOTO_STORY_ID = "hdpic_story";
        // 图片详情
        public static final String SINA_PHOTO_DETAIL_ID = "hdpic_hdpic_toutiao_4";
        // 头条
        public static final String HEADLINE_TYPE = "headline";
        public static final String HEADLINE_ID = "T1348647909107";
    }

    public static class HtmlString {
        public static final String HTML_TITLE = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
                "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
                "<head>\n" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n" +
                "</head>\n" +
                "<body>\n" +
                "<h3><p align=\"center\">";
        public static final String HTML_TITLE_AUTH = "</p></h3>\n<p align=\"center\">";
        public static final String HTML_TITLE_END = "</p>\n<div style=\"text-indent:2em;\">\n";
        public static final String HTML_END = "\n</div></body>\n" + "</html>";
    }

    public static void showToast(Context ctx, String message) {
        if (ctx != null && !TextUtils.isEmpty(message)) {
            Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show();
        }
    }

    public static void showToast(Context ctx, int msgResId) {
        if (ctx != null && msgResId != 0) {
            Toast.makeText(ctx, msgResId, Toast.LENGTH_SHORT).show();
        }
    }
}
