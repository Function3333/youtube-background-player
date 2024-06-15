package com.rest.ybp.utils;

import com.rest.ybp.common.Result;

import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

@Component
public class YoutubeDlUtil {
    private static Properties config;
    
    public YoutubeDlUtil() {
        try {
            InputStream stream = this.getClass().getResourceAsStream("/config.properties");

            config = new Properties();
            config.load(stream);    
        } catch (IOException e) {
            System.out.println("[YoutubeDlUtil] init config failed");
            e.printStackTrace();
        }
    }
    
    public Result extractAudio(String videoId) {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command(
            "yt-dlp",
            "--extract-audio",
            "--audio-format", "mp3",
            "--audio-quality", "0",
            "--output", config.getProperty("audio.savepath") + videoId + ".mp3",
            "--rm-cache-dir",     
            videoId
        );

        try {
            Process process = processBuilder.start();    
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            return Result.SUCCESS;
        } catch (IOException e) {
            e.printStackTrace();
            return Result.EXTRACT_AUDIO_FAIL;
        } 
    }

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
        int videoLength = 0;
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command(
            "yt-dlp",
            "--get-duration",
            videoId
        );

        try {
            Process process = processBuilder.start();    
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while((line = reader.readLine()) != null) {
                videoLength = parseVideLength(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            videoLength = Integer.MAX_VALUE;
        } 

        return videoLength;
    }
}
