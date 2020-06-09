package com.example.image.repo.api;

import com.example.image.repo.Constants;
import com.example.image.repo.model.Image;
import com.example.image.repo.service.AmazonS3ClientService;
import com.example.image.repo.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RestController
@RequestMapping("api/v1/images")
public class FileHandlerController {

    /* dependency injection, let spring deal with the service objects */
    @Autowired
    private AmazonS3ClientService amazonS3ClientService;

    @Autowired
    private ImageService imageService;


    @RequestMapping("/upload")
    @PostMapping
    public Map<String, String> uploadFile(@RequestPart(value = "file")MultipartFile multipartFile,
                                          @RequestPart(value = "description")String description){
        Map<String, String> response = new HashMap<>();
        if (!checkIfValidFile(multipartFile)){
            response.put("error", "this file type is not supported yet");
            return response;
        }
        Image image = new Image(UUID.randomUUID(), multipartFile.getOriginalFilename().toLowerCase(), description.toLowerCase());
        if (imageService.addImage(image)>0){
            this.amazonS3ClientService.uploadFileToS3Bucket(multipartFile, image);
            response.put("message", "file uploading request submitted successfully");
            return response;
        }
        response.put("error", "could not upload the image");
        return response;
    }

    /*remove the file from the sql database*/
    @RequestMapping("/delete")
    @DeleteMapping
    public Map<String, String> deleteFile(@RequestParam("id") UUID id)
    {
        Map<String, String> response = new HashMap<>();
        Image image = imageService.getImageById(id);
        if(imageService.deleteImage(id)>0){
            this.amazonS3ClientService.deleteFileFromS3Bucket(image.getKey());
            response.put("message", "file delete request submitted successfully");
            return response;
        }
        response.put("error", "Could not find an image to delete.");
        return response;
    }

    /*search the sql database*/
    @RequestMapping("/download")
    @GetMapping
    public ResponseEntity<ByteArrayResource> downloadFile(@RequestParam(value = "id") UUID id){
        Image image = imageService.getImageById(id);
        if (image == null){
            String message = "image could not be found";
            return ResponseEntity.badRequest().body(new ByteArrayResource(message.getBytes()));
        }
        byte[] data = amazonS3ClientService.downloadFileFromS3Bucket(image.getKey());
        ByteArrayResource resource = new ByteArrayResource(data);
        return ResponseEntity
                .ok()
                .contentLength(data.length)
                .header("Content-type", "application/octet-stream")
                .header("Content-disposition", "attachment; filename=\"" + image.getKey() + "\"")
                .body(resource);
    }

    @RequestMapping("/search")
    @GetMapping
    public List<Image> searchImage(@RequestParam(value = "keyword") String keyWord){
        return imageService.searchImages(keyWord.toLowerCase());
    }

    public boolean checkIfValidFile(MultipartFile multipartFile){
        String name = multipartFile.getOriginalFilename();
        if(multipartFile!=null){
            String extension = "";
            int i = name.lastIndexOf('.');
            if (i > 0) {
                extension = name.substring(i+1);
            }
            for(String accepted: Constants.ACCEPTED_FILE_TYPES){
                if(extension.equals(accepted)){
                    return true;
                }
            }
            return false;
        }
        return false;
    }
}
