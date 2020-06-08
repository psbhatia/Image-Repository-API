package com.example.image.repo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class Image {

    private final UUID id;
    private final String name;
    private final String description;
    private String key;

    public Image(@JsonProperty("id") UUID id,
                 @JsonProperty("name") String name,
                 @JsonProperty("description")String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        setKey();
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {return description;}

    public String getKey() {return key;}

    /* Key is how the image is stored in AWS, This is unique to all images*/
    public void setKey(){
        StringBuilder sb = new StringBuilder();
        sb.append(this.id.toString());
        String extension = "";
        int i = this.name.lastIndexOf('.');
        if (i > 0) {
            extension = this.name.substring(i+1);
        }
        sb.append(".");
        sb.append(extension);
        this.key = sb.toString();
    }

}
