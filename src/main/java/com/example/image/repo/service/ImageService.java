package com.example.image.repo.service;

import com.example.image.repo.dao.ImageDao;
import com.example.image.repo.model.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ImageService {

    private final ImageDao imageDao;

    //injecting, autowiring into this imageDao interface
    //the fakeDao can be changed to postrges, mongodb etc
    @Autowired
    public ImageService(@Qualifier("postgres") ImageDao imageDao) {
        this.imageDao = imageDao;
    }

    public int addImage(Image image){
        return imageDao.insertImage(image);
    }

    public List<Image> getAllImages(){
        return imageDao.selectAllImage();
    }

    public List<Image> searchImages(String keyword){
        return imageDao.searchImages(keyword);
    }

    public Image getImageById(UUID id){
        return imageDao.selectImageById(id);
    }

    public int deleteImage(UUID id){
        return imageDao.deleteImage(id);
    }

    public int updateImage(UUID id, Image updatedImage){
        return imageDao.updatePersonById(id, updatedImage);
    }

}
