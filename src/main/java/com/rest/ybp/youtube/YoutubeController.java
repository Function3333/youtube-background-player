package com.rest.ybp.youtube;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rest.ybp.common.Response;
import com.rest.ybp.common.Result;
import com.rest.ybp.utils.YoutubeDlUtil;

@RestController
public class YoutubeController {
    private YoutubeDlUtil youtubeDlUtil;
    
    public YoutubeController(YoutubeDlUtil youtubeDlUtil) {
        this.youtubeDlUtil = youtubeDlUtil;
    }

    @GetMapping("/youtubeSearchList")
    public Response getSearchResults(@RequestParam("keyword") String keyword, @RequestParam(value = "searchIdx", required = false) String searchIdx) {
        Response response = null;
        System.out.println("[YoutubeController] keyword :  " + keyword + ", searchIdx : " + searchIdx);
        
        List<Youtube> youtubeList = youtubeDlUtil.getSearchList(keyword, searchIdx);
        response = (youtubeList != null) ? new Response(Result.SUCCESS.getStatus(), youtubeList) 
                                    : new Response(Result.SEARCH_FAIL.getStatus(), Result.SEARCH_FAIL.getMsg());
        return response;
    }
}
