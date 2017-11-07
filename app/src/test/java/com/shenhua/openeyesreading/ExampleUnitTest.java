package com.shenhua.openeyesreading;

import org.junit.Test;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        int random_bg = (int) Math.floor(Math.random() * 99 + 1);
        String bg = "http://meiriyiwen.com/images/new_feed/bg_" + random_bg + ".jpg";
        System.out.println("shenhua sout:" + bg);
    }
}