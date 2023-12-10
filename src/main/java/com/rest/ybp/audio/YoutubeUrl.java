package com.rest.ybp.audio;

import com.rest.ybp.common.VideoType;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;


public class YoutubeUrl {
    private String url;

    private VideoType videoType;

    private List<String> idList;

    private List<String> titleList;

    private Map<String, String> resultMap;

    public YoutubeUrl(String url) {
        this.url = url;
        switch (checkVideoType(url)) {
            case SINGLE -> videoType = VideoType.SINGLE;
            case MULTIPLE -> videoType = VideoType.MULTIPLE;
        }
    }
    public boolean isAllFieldNotNull() {
        return (this.idList != null && this.titleList != null && this.resultMap != null) ? true : false;
    }

    public VideoType checkVideoType(String url) {
        return url.contains("list") ? VideoType.MULTIPLE : VideoType.SINGLE;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public VideoType getVideoType() {
        return videoType;
    }

    public void setVideoType(VideoType videoType) {
        this.videoType = videoType;
    }

    public List<String> getIdList() {
        return idList;
    }

    public void setIdList(List<String> idList) {
        this.idList = idList;
    }

    public List<String> getTitleList() {
        return titleList;
    }

    public void setTitleList(List<String> titleList) {
        this.titleList = titleList;
    }

    public Map<String, String> getResultMap() {
        return resultMap;
    }

    public void setResultMap(Map<String, String> resultMap) {
        this.resultMap = resultMap;
    }
}
