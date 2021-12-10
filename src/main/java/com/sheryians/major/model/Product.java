package com.sheryians.major.model;

import lombok.Data;

import javax.persistence.*;

@Data  // gives getter, setter, etc (Lombok)
@Entity   // makes table
public class Product {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private String name;

    // one category can have multiplpe products
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", referencedColumnName = "category_id")
    private Category category; // can't handle category directly on the front end, so need a dto for product
    private double price;
    private double weight;
    private String description;
    private String imageName;  // db has just name of image, actual image in statics folder, it is costly to store the images

}
