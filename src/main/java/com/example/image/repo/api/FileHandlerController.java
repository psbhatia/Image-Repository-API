package com.example.image.repo.api;

import com.example.image.repo.model.Image;
import com.example.image.repo.service.AmazonS3ClientService;
import com.example.image.repo.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/images")
public class FileHandlerController {

    /* learn what the fuck autowired is */
    @Autowired
    private AmazonS3ClientService amazonS3ClientService;

    @Autowired
    private ImageService imageService;

    /*need to add the file to the sql database*/
    @RequestMapping("/upload")
    @PostMapping
    public Map<String, String> uploadFile(@RequestPart(value = "file")MultipartFile multipartFile,
                                          @RequestPart(value = "desc")String description){
        Map<String, String> response = new HashMap<>();
        Image image = new Image(UUID.randomUUID(), multipartFile.getOriginalFilename(), description);
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
            return (ResponseEntity<ByteArrayResource>) ResponseEntity.badRequest();
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
        return imageService.searchImages(keyWord);
    }
}
