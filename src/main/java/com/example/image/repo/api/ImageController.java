package com.example.image.repo.api;

import com.example.image.repo.model.Image;
import com.example.image.repo.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/* IGNORE THIS CONTROLLER , THIS IS USED FOR TESTING AND NOT A PART OF THE APPLICATION */

@RequestMapping("api/v1/testing")
@RestController
public class ImageController {

    private final ImageService imageService;

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


    @GetMapping(path = "{id}")
    public Image getImageById(@PathVariable("id") UUID id){
        return null;
    }

    @PutMapping(path = "{id}")
    public void updateImage(@PathVariable("id") UUID id, @NonNull @RequestBody Image imagetoUpdate){
        imageService.updateImage(id, imagetoUpdate);
    }


}
