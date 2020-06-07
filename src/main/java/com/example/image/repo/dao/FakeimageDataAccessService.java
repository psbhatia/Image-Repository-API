package com.example.image.repo.dao;

import com.example.image.repo.model.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
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
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FakeimageDataAccessService(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int insertImage(UUID id, Image image) {
//        String sql = "INSERT INTO IMAGE (id,name) VALUES (?, '?')";
//
//        DB.add(new Image(id, image.getName()));
//        return 1;
        if (image.getId() == null){
            return jdbcTemplate.update("INSERT INTO image (id,name) VALUES (?, ?)", UUID.randomUUID(), image.getName());
        }
        return jdbcTemplate.update("INSERT INTO image (id,name) VALUES (?, ?)", image.getId(), image.getName());
    }

    @Override
    public List<Image> selectAllImage() {
        String sql = "SELECT id,name FROM image;";
        List<Image> query = jdbcTemplate.query(sql,((resultSet, i) -> {
            return new Image(
                    UUID.fromString(resultSet.getString("id")),
                    resultSet.getString("name"));
        }));
        return query;
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
