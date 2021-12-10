package com.sheryians.major.dto;

import lombok.Data;

// don't have to create a table, this class just acts as a holder

@Data
public class ProductDTO {

    private Long id;
    private String name;
    private int categoryId; // here we only deal with the ID of the category
    private double price;
    private double weight;
    private String description;
    private String imageName;  // db has just name of image, actual image in statics folder, it is costly to store the images

}
