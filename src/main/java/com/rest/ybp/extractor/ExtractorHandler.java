package com.rest.ybp.extractor;

import com.rest.ybp.common.YoutubeUrl;

import java.io.IOException;

public class ExtractorHandler {

    public static Extractor getExtractor(YoutubeUrl youtubeUrl) {
        switch (youtubeUrl.getVideoType()) {
            case SINGLE -> {
                return new SingleVideoExtractor();
            }
            case MULTIPLE -> {
                return new MultipleVideoExtractor();
            }
        }
        return null;
    }
}
