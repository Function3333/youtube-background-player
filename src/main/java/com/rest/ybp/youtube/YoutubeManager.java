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

    /*
     * keyword validation은 front에서 하기
     * pagination 구현하기
     */
    public String search(String keyword) {
        String result = null;

        try {
            URI uri = new URIBuilder()
                .setScheme("https")
                .setHost("www.googleapis.com/youtube/v3/search")
                .setParameter("key", configProperties.getProperty("youtube.accessToken"))
                .setParameter("q", keyword)
                .setParameter("part", "snippet")
                .setParameter("type", "video")
                .setParameter("maxResult", "10")
                .setParameter("fields", "items(id(videoId), snippet(title,thumbnails.high))")
                .build();
            
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
