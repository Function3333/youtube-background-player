package com.rest.ybp.utils;

import com.rest.ybp.common.Result;
import com.sapher.youtubedl.*;
import org.springframework.stereotype.Component;

import java.nio.file.Paths;

@Component
public class YoutubeDlUtil {
    private static String extractPath = Paths.get(System.getProperty("user.dir"), "audio").toString();
    
    public Result extractAudio(String videoId) {
        try {
            YoutubeDLRequest request = new YoutubeDLRequest(videoId, extractPath);
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

    //in second
    public int parseVideLength(String lawLength) {
        int length = 0;
        
        String[] timeArr = lawLength.trim().split(":");
        
        int arrLength = timeArr.length;
        if(arrLength > 0) {
            switch (arrLength) {
                case 1: 
                    length += Integer.parseInt(timeArr[0]);
                    break;
                case 2:
                    length += Integer.parseInt(timeArr[0]) * 60;
                    length += Integer.parseInt(timeArr[1]);
                    break;
                case 3:
                    length += Integer.parseInt(timeArr[0]) * 3600;
                    length += Integer.parseInt(timeArr[1]) * 60;
                    length += Integer.parseInt(timeArr[2]);
                    break;
                default:
                    length = Integer.MAX_VALUE;
                    break;
            }
        }
        return length;
    }

    public int getVideoLength(String videoId) {
        try {
            YoutubeDLRequest request = new YoutubeDLRequest(videoId);
            request.setOption("get-duration");

            YoutubeDLResponse response = YoutubeDL.execute(request);
            
            return parseVideLength(response.getOut());
        } catch (Exception e) {
            e.printStackTrace();
            return Integer.MAX_VALUE;
        }
    }


}
