package com.example.image.repo.api;

import com.example.image.repo.service.AmazonS3ClientService;
import com.example.image.repo.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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

//    @GetMapping
//    public ResponseEntity<File> downloadFile(@RequestParam(value = "fileName") String fileName){
//        byte[] data = amazonS3ClientService.downloadFileFromS3Bucket(fileName);
//        File outputFile = new File(fileName);
//        try(FileOutputStream outputStream = new FileOutputStream(outputFile);){
//            outputStream.write(data);
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }
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
