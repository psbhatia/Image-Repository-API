package com.example.image.repo.api;

import com.example.image.repo.model.Image;
import com.example.image.repo.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

//This controller is responsible for the incoming requests, it invokes the business logic

@RequestMapping("api/v1/testing")
@RestController
public class ImageController {

    private final ImageService imageService;

    //This is all dependancy injection
    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping
    public void addImage(@RequestBody Image image){
        imageService.addImage(image);
    }

    @GetMapping
    public List<Image> getAllImages() {
        return imageService.getAllImages();
    }

    //should be throwing a 404 instead of null for future
    @GetMapping(path = "{id}")
    public Image getImageById(@PathVariable("id") UUID id){
        return imageService.getImageById(id)
                .orElse(null);
    }

    @DeleteMapping(path = "{id}")
    public void deleteImageById(@PathVariable("id") UUID id){
        imageService.deleteImage(id);
    }

    @PutMapping(path = "{id}")
    public void updateImage(@PathVariable("id") UUID id, @NonNull @RequestBody Image imagetoUpdate){
        imageService.updateImage(id, imagetoUpdate);
    }


}
