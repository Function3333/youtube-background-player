//package com.rest.ybp.audio;
//
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.PersistenceContext;
//import jdk.jfr.Name;
//import org.junit.Assert;
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
//import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.Optional;
//
//@SpringBootTest
//@DataJdbcTest
//public class AudioServiceTest {
//    @Autowired
//    private TestEntityManager tm;
//    @Autowired
//    private AudioRepository audioRepository = new AudioRepository(tm);
//    @Autowired
//    private AudioService audioService;
//
//
//
//
//    /*
//     * Desktop : https://www.youtube.com/watch?v=Epk0CBO3cZk
//     * Mobilde : https://youtu.be/Epk0CBO3cZk?si=loqMeqs5G_kO5yQc
//     * Desktop Playlist :  https://www.youtube.com/watch?v=79c2pSvz8IE&list=RD79c2pSvz8IE&start_radio=1
//     * */
//    @Test
//    @Name("유튜브 url의 id 추출")
//    public void extractIdFromUrl() {
//        String desktopUrl = "https://www.youtube.com/watch?v=Epk0CBO3cZk";
//        String desktopPlayListUrl = "https://www.youtube.com/watch?v=79c2pSvz8IE&list=RD79c2pSvz8IE&start_radio=1";
//        String mobileUrl = "https://youtu.be/Epk0CBO3cZk?si=loqMeqs5G_kO5yQc";
//
//        Optional<String> desktopUrlId = audioService.extractIdFromURL(desktopUrl);
//        Optional<String> desktopPlayListUrlId = audioService.extractIdFromURL(desktopPlayListUrl);
//        Optional<String> mobileUrlId = audioService.extractIdFromURL(mobileUrl);
//
//        Assert.assertEquals(desktopUrlId.get(), "Epk0CBO3cZk");
//        Assert.assertEquals(desktopPlayListUrlId.get(), "79c2pSvz8IE");
//        Assert.assertEquals(mobileUrlId.get(), "Epk0CBO3cZk");
//    }
//}
