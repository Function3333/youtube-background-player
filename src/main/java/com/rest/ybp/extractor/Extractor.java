package com.rest.ybp.extractor;

import com.rest.ybp.common.Result;
import com.rest.ybp.audio.YoutubeUrl;

import java.nio.file.Paths;

public interface Extractor {
    public static final String path = Paths.get(System.getProperty("user.dir"), "audio").toString();
    public Result extractAudio(YoutubeUrl youtubeUrl);
    public void extractId(YoutubeUrl youtubeUrl);
    public void extractTitle(YoutubeUrl youtubeUrl);
    public void generateResultMap(YoutubeUrl youtubeUrl);
}
