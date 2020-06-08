package com.example.image.repo.service;

import com.example.image.repo.model.Image;
import org.springframework.web.multipart.MultipartFile;

public interface AmazonS3ClientService {

    void uploadFileToS3Bucket(MultipartFile multipartFile, Image image);
    void deleteFileFromS3Bucket(String fileName);
    byte[] downloadFileFromS3Bucket(String fileName);
}
