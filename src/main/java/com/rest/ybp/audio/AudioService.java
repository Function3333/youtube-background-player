package com.rest.ybp.audio;

import com.rest.ybp.common.Result;
import com.rest.ybp.utils.S3Util;
import com.rest.ybp.utils.YoutubeDlUtil;
import com.rest.ybp.youtube.Youtube;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Properties;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AudioService {
    private static String extractPath = Paths.get(System.getProperty("user.dir"), "audio").toString();
    private static Properties configProperties;

    private final AudioRepository audioRepository;
    private final S3Util S3Utils;
    private final YoutubeDlUtil extractor;

    public AudioService(AudioRepository audioRepository, S3Util S3Utils, YoutubeDlUtil extractor) throws IOException {
        this.audioRepository = audioRepository;
        this.S3Utils = S3Utils;
        this.extractor = extractor;

        configProperties = new Properties();
        configProperties.load(this.getClass().getResourceAsStream("/config.properties"));
    }

    @Transactional
    public Audio postAudio(Youtube youtube) {
        Audio audio = null;
        int maximumVideoLength = 60 * 10;
        int videoLength = getVideoLength(youtube.getVideoId());

        if(videoLength <= maximumVideoLength) {
            audio = uploadAudio(youtube);
        }
        return audio;
    }

    public Audio uploadAudio(Youtube youtube) {
        Audio audio = null;

        Audio findByYoutubeId = audioRepository.getByYoutubeId(youtube.getVideoId());
        if(findByYoutubeId == null) {
            Result extractAudioResult = extractor.extractAudio(youtube.getVideoId());

            if(extractAudioResult == Result.SUCCESS) {
                Result uploadResult = S3Utils.uploadAudio(youtube.getVideoId() + ".mp3", 
                        new File(extractPath + "/" + youtube.getVideoId() + ".mp3"));
                
                if(uploadResult == Result.SUCCESS) {
                    audio = new Audio(youtube.getVideoId()
                                            ,youtube.getVideoTitle()
                                            ,configProperties.getProperty("aws.bucketUrlPrefix") + youtube.getVideoId() + configProperties.getProperty("aws.bucketUrlPostfix")
                                            ,youtube.getThumbnailUrl());
                                            
                    audioRepository.save(audio);
                }
            }
        } 
        return audio;
    }    

    public int getVideoLength(String videoId) {
        return extractor.getVideoLength(videoId);
    }

    public Audio getAudio(String audioId) {
        Audio audio = null;
        
        try {
            audio = audioRepository.getById(Integer.parseInt(audioId));
        } catch (NumberFormatException e) {
            System.out.println("[AudioService] getAudio Error");
            e.printStackTrace();
        }
        return audio;
    }

    public Audio getByYoutubeId(String youtubeId) {
        return audioRepository.getByYoutubeId(youtubeId);
    }
}
