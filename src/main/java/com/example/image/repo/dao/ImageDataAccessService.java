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

@Repository("postgres")
public class ImageDataAccessService implements ImageDao{

    private static List<Image> DB = new ArrayList<>();
    private final JdbcTemplate jdbcTemplate;
    private String DELIMITER = "?";

    @Autowired
    public ImageDataAccessService(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int insertImage(UUID id, Image image) {
        if (image.getId() == null){
            return jdbcTemplate.update("INSERT INTO image (id,name,description) VALUES (?, ?)", UUID.randomUUID(),
                    image.getName(),image.getDescription());
        }
        return jdbcTemplate.update("INSERT INTO image (id,name,description) VALUES (?, ?, ?)", image.getId(), image.getName(),
                image.getDescription());
    }

    @Override
    public List<Image> selectAllImage() {
        String sql = "SELECT id,name FROM image;";
        List<Image> query = jdbcTemplate.query(sql,((resultSet, i) -> {
            return new Image(
                    UUID.fromString(resultSet.getString("id")),
                    resultSet.getString("name"),resultSet.getString("description"));
        }));
        return query;
    }

    @Override
    public List<Image> searchImages(String keyword) {
        String sql = "SELECT id,name,description FROM image WHERE description LIKE '%";
        StringBuilder sb = new StringBuilder();
        sb.append(sql);
        sb.append(keyword);
        sb.append("%'");
        List<Image> query = jdbcTemplate.query(sb.toString(),((resultSet, i) -> {
            return new Image(
                    UUID.fromString(resultSet.getString("id")),
                    resultSet.getString("name"),resultSet.getString("description"));
        }));
        return query;
    }


    @Override
    public Image selectImageById(UUID id) {
        String sql = "SELECT id,name,description FROM image WHERE id ='";
        StringBuilder sb = new StringBuilder();
        sb.append(sql);
        sb.append(id.toString());
        sb.append("'");
        List<Image> query = jdbcTemplate.query(sb.toString(),((resultSet, i) -> {
            return new Image(
                    UUID.fromString(resultSet.getString("id")),
                    resultSet.getString("name"),resultSet.getString("description"));
        }));
        return query.get(0);
    }

    @Override
    public int deleteImage(UUID id) {
        String sql = "DELETE FROM image WHERE id = '";
        StringBuilder sb = new StringBuilder();
        sb.append(sql);
        sb.append(id.toString());
        sb.append("'");
        return jdbcTemplate.update(sb.toString());
    }

    @Override
    public int updatePersonById(UUID id, Image image) {
        return 0;
    }
}
