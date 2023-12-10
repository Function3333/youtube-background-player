package com.rest.ybp.audio;

import com.rest.ybp.common.Result;
import com.rest.ybp.common.YoutubeUrl;
import com.rest.ybp.extractor.Extractor;
import com.rest.ybp.extractor.ExtractorHandler;
import com.rest.ybp.s3.BucketRepository;
import com.sapher.youtubedl.DownloadProgressCallback;
import com.sapher.youtubedl.YoutubeDL;
import com.sapher.youtubedl.YoutubeDLRequest;
import com.sapher.youtubedl.YoutubeDLResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class AudioService {
    private final AudioRepository audioRepository;
    private final BucketRepository bucketRepository;

    @Autowired
    public AudioService(AudioRepository audioRepository, BucketRepository bucketRepository) {
        this.audioRepository = audioRepository;
        this.bucketRepository = bucketRepository;
    }

    @Transactional
    public Result getAudio(String url) {
        YoutubeUrl youtubeUrl = new YoutubeUrl(url);

        Extractor extractor = ExtractorHandler.getExtractor(youtubeUrl);
        Result extractResult = extractor.extractAudio(youtubeUrl);

        if(extractResult == Result.SUCCESS) {
            extractor.extractId(youtubeUrl);
            extractor.extractTitle(youtubeUrl);
            extractor.generateResultMap(youtubeUrl);

            if(youtubeUrl.isAllFieldNotNull()) {
                List<Audio> audioList = youtubeUrltoAudioList(youtubeUrl);

                for(Audio audio : audioList) {
                    audioRepository.save(audio);

                    Result uploadResult = bucketRepository.uploadAudio(audio.getYoutubeId() + ".mp3", new File("/Users/tomo/Downloads/ybp/audio/" + audio.getYoutubeId() + ".mp3"));
                    if(uploadResult == Result.UPLOAD_AUDIO_FAIL) return Result.UPLOAD_AUDIO_FAIL;
                }
                return Result.SUCCESS;
            } else  {
                return Result.EXTRACT_URL_FAIL;
            }
        }
        return Result.EXTRACT_AUDIO_FAIL;
    }

    public List<Audio> youtubeUrltoAudioList(YoutubeUrl youtubeUrl) {
        Map<String, String> resultMap = youtubeUrl.getResultMap();
        List<Audio> audioList = new ArrayList<>();

        for(String id : resultMap.keySet()) {
            String title = resultMap.get(id);

            Audio audio = new Audio(id, title);
            audioList.add(audio);
        }

        return audioList;
    }
}
