package com.shenhua.comlib.util;

import android.util.Log;

/**
 * 日志工具类
 * Created by shenhua on 8/22/2016.
 */
public class KLog {

    public static final int V = 0x1;
    private static final String NULL_TIPS = "Log with null object.";
    private static final String PARAM = "Param";

    public static void v(Object msg) {
        printLog(V, null, msg);
    }

    private static void printLog(int type, String tagStr, Object... objects) {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        int index = 5;
        String className = stackTraceElements[index].getFileName();
        String methodName = stackTraceElements[index].getMethodName();
        int lineNum = stackTraceElements[index].getLineNumber();
        String methodShortName = methodName.substring(0, 1).toUpperCase() + methodName.substring(1);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[").append(className).append(":").append(lineNum).append("]").append(" ## ").append(methodShortName);
        String tag = tagStr == null ? className : tagStr;
        String msg = objects == null ? NULL_TIPS : getObjectString(objects);
        String log = stringBuilder.toString() + "  " + msg;
        Log.v(tag, log);
    }

    private static String getObjectString(Object... objects) {
        if (objects.length > 1) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("\n");
            for (int i = 0; i < objects.length; i++) {
                Object object = objects[i];
                stringBuilder.append(PARAM).append("[").append(i).append("]").append(" = ");
                if (object == null) stringBuilder.append("null").append("\n");
                else stringBuilder.append(object.toString()).append("\n");
            }
            return stringBuilder.toString();
        } else {
            Object object = objects[0];
            return object == null ? "null" : object.toString();
        }
    }

}
