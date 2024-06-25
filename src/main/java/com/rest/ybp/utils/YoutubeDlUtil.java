package com.rest.ybp.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rest.ybp.common.Result;
import com.rest.ybp.youtube.Youtube;

import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Component
public class YoutubeDlUtil {
    private static final int SEARCH_LIMIT = 5;

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

    public List<Youtube> getSearchList(String keyword) {
        List<Youtube> youtubes = null;

        ProcessBuilder processBuilder = new ProcessBuilder();
        Process process = null;

        // int startIdx = SEARCH_LIMIT * pageIdx + 1;
        // int endIdx = SEARCH_LIMIT * (pageIdx + 1);

        processBuilder.command(
            "yt-dlp",
            "ytsearch6:" + keyword,
            "--skip-download",
            "--no-playlist",
            "--print", 
            // "{\"id\":\"%(id)s\",\"title\":\"%(title)s\",\"thumbnailUrl\":\"%(thumbnail)s\",\"length\":\"%(duration_string)s\"}"
            "{\"id\":\"%(id)s\",\"title\":\"%(title)s\",\"thumbnailUrl\":\"%(thumbnail)s\",\"length\":\"%(duration_string)s\",\"channelTitle\":\"%(uploader)s\"}"
        );
        
        try {
            youtubes = new ArrayList<>();

            process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            ObjectMapper mapper = new ObjectMapper();
            String line;
            
            while((line = reader.readLine()) != null) {
                Youtube youtube = mapper.readValue(line, Youtube.class);
                youtubes.add(youtube);
            }

            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String errorLine;
            while ((errorLine = errorReader.readLine()) != null) {
                System.err.println("youtube DL Search Error: " + errorLine);
            }

            process.destroy();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(process != null) process.destroy();
        }
        return youtubes;
    }
    
    public Result extractAudio(String videoId) {
        ProcessBuilder processBuilder = new ProcessBuilder();
        Process process = null;

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
            process = processBuilder.start();    
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            return Result.SUCCESS;
        } catch (IOException e) {
            e.printStackTrace();
            return Result.EXTRACT_AUDIO_FAIL;
        } finally {
            if(process != null) process.destroy();
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
}
