package com.example.image.repo.api;

import com.example.image.repo.service.AmazonS3ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/files")
public class FileHandlerController {

    /* learn what the fuck autowired is */
    @Autowired
    private AmazonS3ClientService amazonS3ClientService;

    @PostMapping
    public Map<String, String> uploadFile(@RequestPart(value = "file")MultipartFile multipartFile){
        this.amazonS3ClientService.uploadFileToS3Bucket(multipartFile);
        Map<String, String> response = new HashMap<>();
        response.put("message", "file uploading request submitted successfully");
        return response;
    }

    @DeleteMapping
    public Map<String, String> deleteFile(@RequestParam("file_name") String fileName)
    {
        this.amazonS3ClientService.deleteFileFromS3Bucket(fileName);
        Map<String, String> response = new HashMap<>();
        response.put("message", "file delete request submitted successfully");
        return response;
    }
}
