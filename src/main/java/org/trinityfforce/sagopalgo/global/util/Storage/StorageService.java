package org.trinityfforce.sagopalgo.global.util.Storage;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import java.io.IOException;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class StorageService {

    @Value("${cloud.aws.bucket.name}")
    private String bucket;

    @Autowired
    private AmazonS3 s3Client;

    public String uploadFile(MultipartFile file, BucketFolder bucketFolder) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileName = bucketFolder + "/" + UUID.randomUUID() + fileExtension;

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        s3Client.putObject(bucket, fileName, file.getInputStream(), metadata);
        return s3Client.getUrl(bucket, fileName).toString();
    }
}