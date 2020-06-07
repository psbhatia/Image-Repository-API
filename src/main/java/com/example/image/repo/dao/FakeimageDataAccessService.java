package com.example.image.repo.dao;

import com.example.image.repo.model.Image;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

//the @repository indicates that the decorated class is a repository. A repository is a mechanism for encapsulating storeage, retrieveal
// and search behaviour which emulates a collection of objects

@Repository("fakeDao")
public class FakeimageDataAccessService implements ImageDao{

    private static List<Image> DB = new ArrayList<>();

    @Override
    public int insertImage(UUID id, Image image) {
        DB.add(new Image(id, image.getName()));
        return 1;
    }

    @Override
    public List<Image> selectAllImage() {
        return DB;
    }

    @Override
    public Optional<Image> selectImageById(UUID id) {
        return DB.stream()
                .filter(image -> image.getId().equals(id))
                .findFirst();
    }

    @Override
    public int deleteImageById(UUID id) {
        Optional<Image> image = selectImageById(id);
        if (!image.isPresent()){
            return 0;
        }
        DB.remove(image.get());
        return 1;
    }

    @Override
    public int updatePersonById(UUID id, Image image) {
        return selectImageById(id)
                .map(image1 -> {
                    int indexOfImageToUpdate = DB.indexOf(image1);
                    if(indexOfImageToUpdate >= 0){
                        DB.set(indexOfImageToUpdate, new Image(id,image.getName()));
                        return 1;
                    }
                    return 0;
                }).orElse(0);
    }
}
