package com.example.image.repo.dao;

//This is where we define the operations allowed on the image objects
//We can use dependancy injections to switch implementations for this part

import com.example.image.repo.model.Image;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ImageDao {

    //insert image with an id
    int insertImage(UUID id, Image image);

    //insert image without an id
    default int insertImage(Image image){
        UUID id = UUID.randomUUID();
        return insertImage(id, image);
    }

    List<Image> selectAllImage();

    Optional<Image> selectImageById(UUID id);

    int deleteImageById(UUID id);

    int updatePersonById(UUID id, Image image);
}
