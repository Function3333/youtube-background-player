package com.rest.ybp.youtube;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;


@Service
public class YoutubeManager {
    private Properties configProperties;

    public YoutubeManager() throws IOException{
        this.configProperties = new Properties();
        configProperties.load(this.getClass().getResourceAsStream("/config.properties"));
    }

    public String search(String keyword, String nextPageToken) {
        String result = null;

        try {
            URIBuilder uriBuilder = new URIBuilder()
                .setScheme("https")
                .setHost("www.googleapis.com/youtube/v3/search")
                .setParameter("key", configProperties.getProperty("youtube.accessToken"))
                .setParameter("q", keyword)
                .setParameter("part", "snippet")
                .setParameter("type", "video")
                .setParameter("order", "viewCount") // 조회수 높은 순으로 정렬
                .setParameter("regionCode", "KR") // 지역 설정 추가
                .setParameter("fields", "nextPageToken, items(id(videoId), snippet(title,channelTitle, thumbnails.high))")
                .setParameter("maxResult", "5");
                // .setParameter("eventType", "completed") //exclude live streaming
                
            if(nextPageToken != null) {
                uriBuilder.setParameter("pageToken", nextPageToken);
            }

            URI uri = uriBuilder.build();
            HttpGet httpRequest = new HttpGet(uri);

            CloseableHttpClient httpClient = HttpClients.createDefault();
            CloseableHttpResponse response = httpClient.execute(httpRequest);

            HttpEntity entity = response.getEntity();
            if(entity != null) {
                result = EntityUtils.toString(entity);
            }

        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }

        return result;
    } 
}
