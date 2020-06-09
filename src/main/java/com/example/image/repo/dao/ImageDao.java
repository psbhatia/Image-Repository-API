package com.example.image.repo.dao;

//This is where we define the operations allowed on the image objects
//We can use dependancy injections to switch implementations for this part

import com.example.image.repo.model.Image;

import java.util.List;
import java.util.UUID;

public interface ImageDao {

    int insertImage(UUID id, Image image);

    default int insertImage(Image image){
        UUID id = UUID.randomUUID();
        return insertImage(id, image);
    }

    List<Image> selectAllImage();

    List<Image> searchImages(String keyword);

    Image selectImageById(UUID id);

    int deleteImage(UUID id);

    int updatePersonById(UUID id, Image image);
}
