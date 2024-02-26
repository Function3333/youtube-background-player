package com.rest.ybp.audio;

import com.rest.ybp.common.Result;
import com.rest.ybp.extractor.Extractor;
import com.rest.ybp.s3.BucketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class AudioService {
    private final AudioRepository audioRepository;
    private final BucketRepository bucketRepository;
    private final Extractor extractor;

    private static final String BUCKET_URL_PREFIX = "https://tomo-audio-bucket.s3.ap-northeast-2.amazonaws.com/";
    private static final String BUCKET_URL_POSTFIX = ".mp3";

    @Autowired
    public AudioService(AudioRepository audioRepository, BucketRepository bucketRepository, Extractor extractor) {
        this.audioRepository = audioRepository;
        this.bucketRepository = bucketRepository;
        this.extractor = extractor;
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

    @Transactional
    public Result postAudio(String url) {
        YoutubeUrl youtubeUrl = new YoutubeUrl(url);

        return extractor.uploadAudio(youtubeUrl);
    }

    public List<String> getAudioByFullUrl(String url) {
        YoutubeUrl youtubeUrl = new YoutubeUrl(url);
        extractor.extractId(youtubeUrl);

        return youtubeUrl.getIdList();
    }

    public Audio getByYoutubeId(String youtubeId) {
        return audioRepository.getByYoutubeId(youtubeId);
    }

    public List<Audio> youtubeUrltoAudioList(YoutubeUrl youtubeUrl) {
        Map<String, String> resultMap = youtubeUrl.getResultMap();
        List<Audio> audioList = new ArrayList<>();

        for(String id : resultMap.keySet()) {
            String title = resultMap.get(id);

            Audio audio = new Audio(id, title, BUCKET_URL_PREFIX + id + BUCKET_URL_POSTFIX);
            audioList.add(audio);
        }

        return audioList;
    }
}
