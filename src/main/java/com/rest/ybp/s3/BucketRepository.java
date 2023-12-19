package com.rest.ybp.s3;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.rest.ybp.common.Result;

import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Component
public class BucketRepository {

    private final AmazonS3 client;
    private final Properties awsConfig = new Properties();

    public BucketRepository() throws IOException {
        InputStream stream = this.getClass().getResourceAsStream("/aws.properties");
        awsConfig.load(stream);

        AWSCredentials awsCredentials = new BasicAWSCredentials(awsConfig.getProperty("accessKey"), awsConfig.getProperty("privateKey"));
        client = AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .withRegion(Regions.AP_NORTHEAST_2)
                .build();
    }

    public Result uploadAudio(String audioId, File file) {
        try {
            PutObjectRequest request = new PutObjectRequest(awsConfig.getProperty("bucketName"), audioId, file);
            client.putObject(request);
            System.out.printf("[%s] upload complete%n", request.getKey());
        } catch (Exception e) {
            e.printStackTrace();
            return Result.POST_AUDIO_FAIL;
        }
        return Result.SUCCESS;
    }
}
