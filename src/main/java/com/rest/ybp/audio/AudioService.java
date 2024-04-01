package com.rest.ybp.audio;

import com.rest.ybp.common.Result;
import com.rest.ybp.extractor.Extractor;
import com.rest.ybp.utils.s3Util;
import com.rest.ybp.youtube.Youtube;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AudioService {
    private final AudioRepository audioRepository;
    private final s3Util bucketRepository;
    private final Extractor extractor;

    private static final String BUCKET_URL_PREFIX = "https://tomo-audio-bucket.s3.ap-northeast-2.amazonaws.com/";
    private static final String BUCKET_URL_POSTFIX = ".mp3";


    public AudioService(AudioRepository audioRepository, s3Util bucketRepository, Extractor extractor) {
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
    public Result postAudio(Youtube youtube) {
        return extractor.uploadAudio(youtube);
    }

    // public List<String> getAudioByFullUrl(String url) {
    //     Youtube youtubeUrl = new Youtube(url);
    //     extractor.extractId(youtubeUrl);

    //     return youtubeUrl.getIdList();
    // }

    public Audio getByYoutubeId(String youtubeId) {
        return audioRepository.getByYoutubeId(youtubeId);
    }

    // public List<Audio> youtubeUrltoAudioList(Youtube youtubeUrl) {
    //     Map<String, String> resultMap = youtubeUrl.getResultMap();
    //     List<Audio> audioList = new ArrayList<>();

    //     for(String id : resultMap.keySet()) {
    //         String title = resultMap.get(id);

    //         Audio audio = new Audio(id, title, BUCKET_URL_PREFIX + id + BUCKET_URL_POSTFIX);
    //         audioList.add(audio);
    //     }

    //     return audioList;
    // }
}
