package com.shenhua.openeyesreading.core;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Http请求管理类
 * Created by shenhua on 8/23/2016.
 */
public class HttpManager {

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
