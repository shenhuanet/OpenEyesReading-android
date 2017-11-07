package com.shenhua.openeyesreading.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

/**
 * 新浪图片详情实体
 * Created by shenhua on 8/25/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SinaPhotoDetail implements Serializable {

    private static final long serialVersionUID = -1847081854870835058L;

    @JsonProperty("status")
    public int status;

    @JsonProperty("data")
    public SinaPhotoDetailDataEntity data;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SinaPhotoDetailDataEntity implements Serializable {
        private static final long serialVersionUID = 7122857266964883940L;
        @JsonProperty("id")
        public String id;
        @JsonProperty("title")
        public String title;
        @JsonProperty("long_title")
        public String longTitle;
        @JsonProperty("source")
        public String source;
        @JsonProperty("link")
        public String link;
        @JsonProperty("comments")
        public String comments;
        @JsonProperty("need_match_pic")
        public boolean needMatchPic;
        @JsonProperty("pubDate")
        public int pubDate;
        @JsonProperty("lead")
        public String lead;
        @JsonProperty("content")
        public String content;
        @JsonProperty("keys")
        public List<?> keys;
        @JsonProperty("videos")
        public List<?> videos;
        @JsonProperty("pics")
        public List<SinaPhotoDetailPicsEntity> pics;
        @JsonProperty("recommends")
        public List<?> recommends;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SinaPhotoDetailPicsEntity implements Serializable {
        private static final long serialVersionUID = -6044159681078900542L;
        @JsonProperty("pic")
        public String pic;
        @JsonProperty("alt")
        public String alt;
        @JsonProperty("kpic")
        public String kpic;
        @JsonProperty("size")
        public String size;

        public boolean showTitle = true;
    }


}
