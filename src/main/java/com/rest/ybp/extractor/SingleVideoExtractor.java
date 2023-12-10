package com.rest.ybp.extractor;

import com.rest.ybp.common.Result;
import com.rest.ybp.audio.YoutubeUrl;
import com.sapher.youtubedl.*;

import java.util.*;

public class SingleVideoExtractor implements Extractor {

    @Override
    public Result extractAudio(YoutubeUrl youtubeUrl) {
        try {
            YoutubeDLRequest request = new YoutubeDLRequest(youtubeUrl.getUrl(), Extractor.path);
            request.setOption("extract-audio");
            request.setOption("id");
            request.setOption("audio-format", "mp3");
            request.setOption("retries", 10);
            request.setOption("ignore-errors");
            request.setOption("rm-cache-dir");
            //           request.setOption("get-id");
            //           request.setOption("output", fileName + ".%(ext)s");
            //           request.setOption("flat-playlist");
            //           request.setOption("no-part");

            YoutubeDLResponse response = YoutubeDL.execute(request, new DownloadProgressCallback() {
                @Override
                public void onProgressUpdate(float progress, long etaInSeconds) {
                    //여기에서 진행률을 반환 할 메서드 추가하기
                    System.out.println(progress + "%");
                }
            });
            return Result.SUCCESS;
        } catch (Exception e) {
            System.out.println("[SingleVideoExtractor] extractAudio() Error : " + e);
            return Result.EXTRACT_AUDIO_FAIL;
        }
    }


    @Override
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


    @Override
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

    @Override
    public void generateResultMap(YoutubeUrl youtubeUrl) {
        List<String> idList = youtubeUrl.getIdList();
        List<String> titleList = youtubeUrl.getTitleList();

        HashMap<String, String> resultMap = new LinkedHashMap<>();
        if(idList.size() == titleList.size()) {
            for(int idx = 0; idx < idList.size(); idx++) {
                resultMap.put(idList.get(idx), titleList.get(idx));
            }
        }

        youtubeUrl.setResultMap(resultMap);
    }

    public List<String> responseToList(String responseGetOut) {
        return Arrays.asList(responseGetOut.split("\n"));
    }
}
