package com.rest.ybp.extractor;

import com.rest.ybp.audio.Audio;
import com.rest.ybp.audio.AudioRepository;
import com.rest.ybp.common.Result;
import com.rest.ybp.utils.s3Util;
import com.rest.ybp.youtube.Youtube;
import com.sapher.youtubedl.*;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Properties;

@Component
public class Extractor {
    private static Properties configProperties;
    private static String extractPath = Paths.get(System.getProperty("user.dir"), "audio").toString();
    
    private AudioRepository audioRepository;
    private s3Util bucketRepository;

    public Extractor(AudioRepository audioRepository, s3Util bucketRepository) throws IOException {
        this.audioRepository = audioRepository;
        this.bucketRepository = bucketRepository;

        configProperties = new Properties();
        configProperties.load(this.getClass().getResourceAsStream("/config.properties"));
    }

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

    public Result uploadAudio(Youtube youtube) {
        Audio findByYoutubeId = audioRepository.getByYoutubeId(youtube.getVideoId());

        if(findByYoutubeId == null) {
            Result extractAudioResult = extractAudio(youtube.getVideoId());

            if(extractAudioResult == Result.SUCCESS) {
                Result uploadResult = bucketRepository.uploadAudio(youtube.getVideoId() + ".mp3", 
                        new File(extractPath + "/" + youtube.getVideoId() + ".mp3"));
                
                if(uploadResult == Result.SUCCESS) {
                    Audio audio = new Audio(youtube.getVideoId()
                                            ,youtube.getVideoTitle()
                                            ,configProperties.getProperty("aws.bucketUrlPrefix") + youtube.getVideoId() + configProperties.getProperty("aws.bucketUrlPostfix"));
                                            
                    audioRepository.save(audio);
                    return Result.SUCCESS;
                }
            }
        }
        return Result.EXTRACT_AUDIO_FAIL;
    }
}
