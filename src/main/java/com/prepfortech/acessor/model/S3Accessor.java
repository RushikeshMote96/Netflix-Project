package com.prepfortech.acessor.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.model.GetObjectAclRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.time.Duration;

@Repository
public class S3Accessor {

    private final String bucketName = "pft_netflix_demo";

    @Autowired
    private AwsCredentialsProvider awsCredentialsProvider;

    public String getPreSignedUrl(final String path, final int DurationInSeconds){
        S3Presigner presigner = S3Presigner
                .builder()
                .region(Region.AF_SOUTH_1)
                .credentialsProvider(awsCredentialsProvider)
                .build();

        GetObjectRequest getObjectAclRequest = GetObjectRequest
                .builder()
                .key(path)
                .bucket(bucketName)
                .build();
        GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest
                .builder()
                .signatureDuration(Duration.ofSeconds(DurationInSeconds))
                .getObjectRequest(getObjectAclRequest)
                .build();

        PresignedGetObjectRequest request = presigner.presignGetObject(getObjectPresignRequest);

        return request.url().toString();

    }
}
