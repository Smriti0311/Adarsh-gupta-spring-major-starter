package com.sheryians.major.model;

import lombok.Data;

import javax.persistence.*;

// Lombok helps access getter and setter methods directly

@Entity
@Data
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "category_id")
    private int id;

    private String name;

}
