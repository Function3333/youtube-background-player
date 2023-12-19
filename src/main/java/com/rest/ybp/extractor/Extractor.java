package com.rest.ybp.extractor;

import com.rest.ybp.audio.Audio;
import com.rest.ybp.audio.AudioRepository;
import com.rest.ybp.common.Result;
import com.rest.ybp.audio.YoutubeUrl;
import com.rest.ybp.s3.BucketRepository;
import com.sapher.youtubedl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.nio.file.Paths;
import java.util.*;

@Component
public class Extractor {
    private AudioRepository audioRepository;
    private BucketRepository bucketRepository;

    private static final String bucketUrlPrefix = "https://tomo-audio-bucket.s3.ap-northeast-2.amazonaws.com/";
    private static final String bucketUrlPostfix = ".mp3";
    private static final String extractPath = Paths.get(System.getProperty("user.dir"), "audio").toString();

    @Autowired
    public Extractor(AudioRepository audioRepository, BucketRepository bucketRepository) {
        this.audioRepository = audioRepository;
        this.bucketRepository = bucketRepository;
    }

    public Result extractAudio(String url) {
        try {
            YoutubeDLRequest request = new YoutubeDLRequest(url, extractPath);
            request.setOption("extract-audio");
            request.setOption("id");
            request.setOption("audio-format", "mp3");
            request.setOption("retries", 10);
            request.setOption("ignore-errors");
            request.setOption("rm-cache-dir");
            YoutubeDLResponse response = YoutubeDL.execute(request, new DownloadProgressCallback() {
                @Override
                public void onProgressUpdate(float progress, long etaInSeconds) {
                    //여기에서 진행률을 반환 할 메서드 추가하기
                    System.out.println(progress + "%");
                }
            });
            return Result.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return Result.EXTRACT_AUDIO_FAIL;
        }
    }
    public Result uploadAudio(YoutubeUrl youtubeUrl) {
        extractId(youtubeUrl);
        extractTitle(youtubeUrl);
        generateResultMap(youtubeUrl);

        if(youtubeUrl.isAllFieldNotNull()) {
            try {
                for(String youtubeId : youtubeUrl.getIdList()) {
                    Audio findByYoutubeId = audioRepository.getByYoutubeId(youtubeId);

                    if(findByYoutubeId == null) {
                        Result extractAudioResult = extractAudio(youtubeIdToUrl(youtubeId));

                        if(extractAudioResult == Result.SUCCESS) {
                            Result uploadResult = bucketRepository.uploadAudio(youtubeId + ".mp3", 
                                    new File("/Users/tomo/Downloads/ybp/audio/" + youtubeId+ ".mp3"));
                            
                            if(uploadResult == Result.SUCCESS) {
                                Map<String, String> resultMap =youtubeUrl.getResultMap();
                                String youtubeTitle = resultMap.get(youtubeId);

                                Audio audio = new Audio(youtubeId, youtubeTitle, bucketUrlPrefix + youtubeId + bucketUrlPostfix);
                                audioRepository.save(audio);
                            }
                        }
                    }
                }
                return Result.SUCCESS;
            } catch (Exception e) {
                System.out.println("[SingleVideoExtractor] uplodaAudio Error");
                e.printStackTrace();
            }
        }
        return Result.EXTRACT_AUDIO_FAIL;
    }

    public void extractId(YoutubeUrl youtubeUrl) {
        try {
            YoutubeDLRequest request = new YoutubeDLRequest(youtubeUrl.getUrl());
            request.setOption("encoding", "utf-8");
            request.setOption("get-id");

            YoutubeDLResponse response = YoutubeDL.execute(request);
            youtubeUrl.setIdList(responseToList(response.getOut()));
        } catch (Exception e) {
            System.out.println("[SingleVideoExtractor] extractId() Error : " + e);
        }
    }
    public void extractTitle(YoutubeUrl youtubeUrl) {
        try {
            YoutubeDLRequest request = new YoutubeDLRequest(youtubeUrl.getUrl());
            request.setOption("encoding", "utf-8");
            request.setOption("get-title");

            YoutubeDLResponse response = YoutubeDL.execute(request);
            youtubeUrl.setTitleList(responseToList(response.getOut()));
        } catch (Exception e) {
            System.out.println("[SingleVideoExtractor] extractTitle() Error : " + e);
        }
    }

    public void generateResultMap(YoutubeUrl youtubeUrl) {
        List<String> idList = youtubeUrl.getIdList();
        List<String> titleList = youtubeUrl.getTitleList();

        HashMap<String, String> resultMap = null;
        if(idList.size() == titleList.size()) {
            resultMap = new LinkedHashMap<>();

            for(int idx = 0; idx < idList.size(); idx++) {
                resultMap.put(idList.get(idx), titleList.get(idx));
            }
        }

        youtubeUrl.setResultMap(resultMap);
    }

    public String youtubeIdToUrl(String youtubeId) {
        String urlPrefix = "https://www.youtube.com/watch?v=";

        return urlPrefix + youtubeId;
    }
    public List<String> responseToList(String responseGetOut) {
        return Arrays.asList(responseGetOut.split("\n"));
    }
}
