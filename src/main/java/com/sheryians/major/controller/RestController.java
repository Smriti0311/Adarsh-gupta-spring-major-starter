package com.sheryians.major.controller;

import com.sheryians.major.model.Category;
import com.sheryians.major.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@org.springframework.web.bind.annotation.RestController
public class RestController {

    @Autowired
    CategoryService categoryService;


    @GetMapping("/shop/category2/{id}")
    public List<Category> shopByCateory2(@PathVariable int id){
//        model.addAttribute("categories", categoryService.showAllCategories());
//        model.addAttribute("products", productService.getAllProductsByCategoryId(id));
        return categoryService.showAllCategories();
    }
}
