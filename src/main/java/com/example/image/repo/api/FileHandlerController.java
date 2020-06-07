package com.example.image.repo.api;

import com.example.image.repo.service.AmazonS3ClientService;
import com.example.image.repo.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/v1/images")
public class FileHandlerController {

    /* learn what the fuck autowired is */
    @Autowired
    private AmazonS3ClientService amazonS3ClientService;

    @Autowired
    private ImageService imageService;

    /*need to add the file to the sql database*/
    @PostMapping
    public Map<String, String> uploadFile(@RequestPart(value = "file")MultipartFile multipartFile){
        this.amazonS3ClientService.uploadFileToS3Bucket(multipartFile);
        Map<String, String> response = new HashMap<>();
        response.put("message", "file uploading request submitted successfully");
        return response;
    }

    /*remove the file from the sql database*/
    @DeleteMapping
    public Map<String, String> deleteFile(@RequestParam("file_name") String fileName)
    {
        this.amazonS3ClientService.deleteFileFromS3Bucket(fileName);
        Map<String, String> response = new HashMap<>();
        response.put("message", "file delete request submitted successfully");
        return response;
    }

    /*search the sql database*/
    @GetMapping
    public ResponseEntity<ByteArrayResource> downloadFile(@RequestParam(value = "fileName") String key){
        byte[] data = amazonS3ClientService.downloadFileFromS3Bucket(key);
        ByteArrayResource resource = new ByteArrayResource(data);
        return ResponseEntity
                .ok()
                .contentLength(data.length)
                .header("Content-type", "application/octet-stream")
                .header("Content-disposition", "attachment; filename=\"" + key + "\"")
                .body(resource);
    }
}
