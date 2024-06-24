package com.rest.ybp.audio;

import com.rest.ybp.common.Result;
import com.rest.ybp.utils.S3Util;
import com.rest.ybp.utils.YoutubeDlUtil;
import com.rest.ybp.youtube.Youtube;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AudioService {
    private static Properties configProperties;

    private final AudioRepository audioRepository;
    private final YoutubeDlUtil extractor;
    private final S3Util S3Utils;
    

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
        int videoLength = getVideoLength(youtube.getId());

        if(videoLength <= maximumVideoLength) {
            audio = uploadAudio(youtube);
        }

        return audio;
    }

    public Audio uploadAudio(Youtube youtube) {
        
        Audio audio = audioRepository.getByYoutubeId(youtube.getId());
        if(audio == null) {
            Result extractAudioResult = extractor.extractAudio(youtube.getId());

            if(extractAudioResult == Result.SUCCESS) {
                File uploadFile = new File(configProperties.getProperty("audio.savepath") + youtube.getId() + ".mp3");
                Result uploadResult = S3Utils.uploadAudio(youtube.getId() + ".mp3", uploadFile);
                
                if(uploadResult == Result.SUCCESS) {
                    audio = new Audio(youtube.getId()
                                            ,youtube.getTitle()
                                            ,configProperties.getProperty("aws.bucketUrlPrefix") + youtube.getId() + configProperties.getProperty("aws.bucketUrlPostfix")
                                            ,youtube.getThumbnailUrl());
                                            
                    audioRepository.save(audio);
                }
                deleteFile(uploadFile);
            }
        }
        return audio;
    }    

    public void deleteFile(File file) {
        if(file.exists()) {
            boolean deleteResult = file.delete();

            if(deleteResult) System.out.println("[AudioService] Delete Uploaded File Success");
            else System.out.println("[AudioService] Delete Uploaded File Fail");
        }
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
