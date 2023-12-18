package com.rest.ybp.audio;

import com.rest.ybp.common.Result;
import com.rest.ybp.extractor.Extractor;
import com.rest.ybp.extractor.ExtractorHandler;
import com.rest.ybp.s3.BucketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class AudioService {
    private final AudioRepository audioRepository;
    private final BucketRepository bucketRepository;
    private static final String bucketUrlPrefix = "https://tomo-audio-bucket.s3.ap-northeast-2.amazonaws.com/";
    private static final String bucketUrlPostfix = ".mp3";

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

            Audio audio = new Audio(id, title, bucketUrlPrefix + id + bucketUrlPostfix);
            audioList.add(audio);
        }

        return audioList;
    }
}
