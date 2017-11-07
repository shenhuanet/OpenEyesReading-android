package com.shenhua.openeyesreading.core;

import android.os.Handler;
import android.os.Message;

/**
 * 业务请求回调接口
 * Created by shenhua on 2016/7/28.
 */
public abstract class HttpApiCallback<T> extends Handler {

    /**
     * 在业务请求成功后调用
     */
    public abstract void onSuccess(T t);

    /**
     * 在业务请求失败后调用
     */
    public abstract void onFailed(int errorCode, String msg);

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case HttpApiImpl.SUCCESS:
                // msg.obj是回调返对象
                onSuccess((T) msg.obj);
                break;
            case HttpApiImpl.FAILED:
                // msg.arg1是errorCode
                onFailed(msg.arg1, msg.obj.toString());
                break;
        }
    }
}