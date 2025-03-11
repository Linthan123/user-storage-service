package com.example.user_storage_service.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StorageService {

    private final S3Client s3Client;
    private final String bucketName;

    public StorageService(S3Client s3Client, @Value("${aws.s3.bucket-name}") String bucketName) {
        this.s3Client = s3Client;
        this.bucketName = bucketName;
    }

    // ✅ Upload File to S3
    public String uploadFile(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new RuntimeException("File is empty");
            }

            String key = "uploads/" + file.getOriginalFilename();

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType(file.getContentType()) // Fix content-type issue
                    // .acl(ObjectCannedACL.PUBLIC_READ) // Make file public (optional)
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));

            return "https://" + bucketName + ".s3.amazonaws.com/" + key;
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file: " + e.getMessage(), e);
        }
    }

    // ✅ Search Files in S3
    public List<String> searchFiles(String userName, String searchTerm) {
        String userFolder = userName + "/";

        ListObjectsV2Request request = ListObjectsV2Request.builder()
                .bucket(bucketName)
                .prefix(userFolder)
                .build();

        ListObjectsV2Response response = s3Client.listObjectsV2(request);

        return response.contents().stream()
                .map(S3Object::key)
                .filter(key -> key.contains(searchTerm))
                .collect(Collectors.toList());
    }
}

// package com.example.user_storage_service.service;

// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.stereotype.Service;
// import org.springframework.web.multipart.MultipartFile;

// // import java.io.File;
// import java.io.IOException;
// import java.nio.file.Files;
// import java.nio.file.Path;
// import java.nio.file.Paths;

// @Service
// public class StorageService {

// private final Path uploadDir;

// public StorageService(@Value("${file.upload-dir}") String uploadDir) {
// this.uploadDir = Paths.get(uploadDir).toAbsolutePath().normalize();
// try {
// Files.createDirectories(this.uploadDir);
// } catch (IOException e) {
// throw new RuntimeException("Failed to create upload directory", e);
// }
// }

// public String uploadFile(MultipartFile file) {
// try {
// if (file.isEmpty()) {
// throw new RuntimeException("File is empty");
// }

// // Save the file to the local storage directory
// Path filePath = uploadDir.resolve(file.getOriginalFilename());
// file.transferTo(filePath.toFile());

// return "File uploaded successfully: " + filePath.toString();
// } catch (IOException e) {
// throw new RuntimeException("Failed to store file", e);
// }
// }
// }
