package com.shenhua.openeyesreading.core;

import com.shenhua.openeyesreading.bean.NewsData;
import com.shenhua.openeyesreading.bean.SinaPhotoDetail;
import com.shenhua.openeyesreading.bean.SinaPhotosList;

import java.util.List;
import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 请求数据服务接口
 * Created by shenhua on 8/23/2016.
 */
public interface HttpService {

    @GET("list.json")
    Observable<SinaPhotosList> getSinaPhotosList(
            @Header("Cache-Control") String cacheControl,
            @Query("channel") String photoTypeId,
            @Query("adid") String adid,
            @Query("wm") String wm,
            @Query("from") String from,
            @Query("chwm") String chwm,
            @Query("oldchwm") String oldchwm,
            @Query("imei") String imei,
            @Query("uid") String uid,
            @Query("p") int page
    );

    @GET("article.json")
    Observable<SinaPhotoDetail> getSinaPhotoDetal(
            @Header("Cache-Control") String cacheControl,
            @Query("postt") String postt,
            @Query("wm") String wm,
            @Query("from") String from,
            @Query("chwm") String chwm,
            @Query("oldchwm") String oldchwm,
            @Query("imei") String imei,
            @Query("uid") String uid,
            @Query("id") String id
    );

    @GET("nc/article/{type}/{id}/{startPage}-20.html")
    Observable<Map<String, List<NewsData>>> getNewsList(
            @Header("Cache-Control") String cacheControl,
            @Path("type") String type,
            @Path("id") String id,
            @Path("startPage") int startPage
    );


}
