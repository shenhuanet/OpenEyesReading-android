package com.shenhua.openeyesreading.core;

import android.support.annotation.NonNull;
import android.util.SparseArray;

import com.shenhua.comlib.AppUtils;
import com.shenhua.comlib.util.NetworkUtils;
import com.shenhua.openeyesreading.App;
import com.shenhua.openeyesreading.bean.NewsData;
import com.shenhua.openeyesreading.bean.SinaPhotoDetail;
import com.shenhua.openeyesreading.bean.SinaPhotosList;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Retrofit请求管理类
 * Created by shenhua on 8/23/2016.
 */
public class RetrofitManager {

    public static final long CACHE_STALE_SEC = 60 * 60 * 24 * 2;//缓存有效期为2天
    // 查询缓存的Cache-Control设置，为if-only-cache时只查询缓存而不会请求服务器，max-stale可以配合设置缓存失效时间
    public static final String CACHE_CONTROL_CACHE = "only-if-cached,max-stale=" + CACHE_STALE_SEC;
    public static final String CACHE_CONTROL_NETWORK = "max-age=0";
    private static SparseArray<RetrofitManager> mInstanceManager = new SparseArray<>(HostType.TYPE_COUNT);
    private HttpService service;
    private static volatile OkHttpClient mOkHttpClient;

    public static RetrofitManager getInstance(int hostType) {
        RetrofitManager instance = mInstanceManager.get(hostType);
        if (instance == null) {
            instance = new RetrofitManager(hostType);
            mInstanceManager.put(hostType, instance);
            return instance;
        } else return instance;
    }

    public RetrofitManager(@HostType.HostTypeChecker int hostType) {
        initOkHttpClient();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(getHost(hostType))
                .client(mOkHttpClient)
                .addConverterFactory(JacksonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        service = retrofit.create(HttpService.class);
    }

    private void initOkHttpClient() {
        if (mOkHttpClient == null) {
            synchronized (RetrofitManager.class) {
                Cache cache = new Cache(new File(App.getContext().getCacheDir(), "HttpCache"), 1024 * 1024 * 100);
                mOkHttpClient = new OkHttpClient.Builder().cache(cache)
                        .addNetworkInterceptor(new RewriteCacheControlInterceptor())
                        .addInterceptor(new RewriteCacheControlInterceptor())
                        .addInterceptor(new JsonResultInterceptor())
                        .retryOnConnectionFailure(true)
                        .connectTimeout(30, TimeUnit.SECONDS)
                        .build();
            }
        }
    }

    private String getHost(int hostType) {
        switch (hostType) {
            case HostType.NEWS:
                return AppUtils.Constants.HOST_NEWS;
            case HostType.SINA_PHOTOS:
                return AppUtils.Constants.HOST_SINA_PHOTOS;
        }
        return "";
    }

    @NonNull
    private String getCacheControl() {
        return NetworkUtils.isConnected(App.getContext()) ? CACHE_CONTROL_NETWORK : CACHE_CONTROL_CACHE;
    }

    public Observable<SinaPhotosList> getSinaPhotoListObservable(String photoTypeId, int page) {
        return service.getSinaPhotosList(getCacheControl(), photoTypeId,
                "4ad30dabe134695c3b7c3a65977d7e72", "b207", "6042095012", "12050_0001",
                "12050_0001", "867064013906290", "802909da86d9f5fc", page)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

    public Observable<SinaPhotoDetail> getSinaPhotoDetailObservable(String id) {
        return service.getSinaPhotoDetal(getCacheControl(), AppUtils.Constants.SINA_PHOTO_DETAIL_ID,
                "B207", "6042095012", "12050_0001", "12050_0001", "867064013906290",
                "802909da86d9f5fc", id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

    public Observable<Map<String, List<NewsData>>> getNewsListObservable(String type, String id, int startPage) {
        return service.getNewsList(getCacheControl(), type, id, startPage).subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).unsubscribeOn(Schedulers.io());
    }

    /**
     * 云端响应头拦截器，用来配置缓存策略
     */
    private class RewriteCacheControlInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            if (!NetworkUtils.isConnected(App.getContext()))
                request = request.newBuilder().cacheControl(CacheControl.FORCE_CACHE).build();
            Response response = chain.proceed(request);
            if (NetworkUtils.isConnected(App.getContext()))
                return response.newBuilder().header("Cache-Control", request.cacheControl().toString()).removeHeader("Pragma").build();
            else
                return response.newBuilder().header("Cache-Control", "public,only-if-cached," + CACHE_STALE_SEC).removeHeader("Pragma").build();
        }
    }

    /**
     * json数据返回打印拦截头，用于debug
     */
    private class JsonResultInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Response response = chain.proceed(request);
            ResponseBody body = response.body();
            long length = body.contentLength();
            BufferedSource source = body.source();
            source.request(Long.MAX_VALUE);
            Buffer buffer = source.buffer();
            Charset charset = Charset.forName("UTF-8");
            MediaType type = body.contentType();
            if (type != null) {
                try {
                    charset = type.charset(charset);
                } catch (UnsupportedCharsetException e) {
                    e.printStackTrace();
                    return response;
                }
            }
            if (length != 0) {
                System.out.println("shenhua sout:--------------------------------------------开始打印返回数据----------------------------------------------------");
                System.out.println("shenhua sout:" + buffer.clone().readString(charset));
            }
            return response;
        }
    }

}
