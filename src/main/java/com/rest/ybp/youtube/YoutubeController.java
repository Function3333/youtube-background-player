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

    @GetMapping("/youtubeSearchList")
    public Response getSearchResults(@RequestParam("keyword") String keyword, @RequestParam(value = "nextPageToken", required = false) String nextPageToken) {
        System.out.println("[YoutubeController] Search Keyword : " + keyword);
        System.out.println("[YoutubeController] Next Page Token : " + nextPageToken);

        Response response = null;
        
        String result = youtubeManager.search(keyword, nextPageToken);
        response = (result != null) ? new Response(Result.SUCCESS.getStatus(), result) 
                                    : new Response(Result.SEARCH_FAIL.getStatus(), Result.SEARCH_FAIL.getMsg());

        return response;
    }
}
