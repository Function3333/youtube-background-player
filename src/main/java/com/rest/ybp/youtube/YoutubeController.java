package com.rest.ybp.youtube;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rest.ybp.common.Response;
import com.rest.ybp.common.Result;

@RestController
public class YoutubeController {
    private YoutubeManager youtubeManager;
    
    public YoutubeController(YoutubeManager youtubeManager) {
        this.youtubeManager = youtubeManager;
    }

    @GetMapping("/youtube/searchList")
    public Response getSearchResults(@RequestParam("keyword") String keyword) {
        Response response;
        
        String result = youtubeManager.search(keyword);
        response = (result != null) ? new Response(Result.SUCCESS.getStatus(), result) 
                                    : new Response(Result.SEARCH_FAIL.getStatus(), Result.SEARCH_FAIL.getMsg());

        return response;
    }
}
