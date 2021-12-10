package com.sheryians.major.controller;


import com.sheryians.major.dto.ProductDTO;
import com.sheryians.major.model.Category;
import com.sheryians.major.model.Product;
import com.sheryians.major.service.CategoryService;
import com.sheryians.major.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Controller
public class AdminController {

    public static String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/productImages";

    @Autowired
    CategoryService categoryService;

    @Autowired
    ProductService productService;

    @GetMapping("/admin")
    public String adminHome(){
        return "adminHome";
    }

    @GetMapping("/admin/categories")
    public String getCategories(Model model){
        model.addAttribute("categories", categoryService.showAllCategories());
        return "categories";
    }

    // when tried to go to this route on localhost, it gave error
    // as categoriesAdd.html expects an object of type category
    // so now we need to create a "model" for it in the model repository (like in Django)
    // model basically is the table
    // after creating model, we create a repository for category
    // which is actually an interface
    // then we create a service for it as a middleware
    @GetMapping("/admin/categories/add")
    public String getAddToCategories(Model model){

        // the key is "category" from categoriesAdd.html and the value is a new Category object
        model.addAttribute("category", new Category());
        return "categoriesAdd";
    }

    @PostMapping("/admin/categories/add")
    // the key is "category" from categoriesAdd.html and the value is a new Category object
    public String postAddToCategories(@ModelAttribute("category") Category category){
        categoryService.addCategory(category);
        return "redirect:/admin/categories";

    }

    // delete method not supported for views (like DELETE) as it is not supported
    // but it works with REST APIs

    @GetMapping("/admin/categories/delete/{id}")
    public String deleteCategory(@PathVariable int id){

        categoryService.removeCategoryByID(id);
        return "redirect:/admin/categories";
    }


    // save and update works in the same way
    // if an obj with id=33 is present in db,
    // instead od adding new obj, update it
    // else give a new id and add

    @GetMapping("/admin/categories/update/{id}")
    public String updateCategory(@PathVariable int id, Model model){

        Optional<Category> category = categoryService.getCategoryById(id);
        if(category.isPresent()){
            model.addAttribute("category", category.get());
            return "addCategories";
        }
        else{
            return "404";
        }
    }


    // PRODUCT Section
    @GetMapping("/admin/products")
    public String getProducts(Model model){
        model.addAttribute("products", productService.showAllProducts());
        return "products";
    }

    @GetMapping("/admin/products/add")
    public String addProductsGet(Model model){
        model.addAttribute("productDTO", new ProductDTO());
        model.addAttribute("categories", categoryService.showAllCategories());
        return "productsAdd";
    }

    @PostMapping("/admin/products/add")
    public String addProductsPost(@ModelAttribute("productDTO")ProductDTO productDTO,
                                  @RequestParam("productImage")MultipartFile file,
                                  @RequestParam("imgName")String imgName) throws IOException {

        // because Lombok isused in ProductDTO class, we ge the functionality of the getter and setter methods
        // need to convert productDTO -> product
        // for submitting all types of files, we use multipart
        Product product = new Product();
        product.setId(productDTO.getId());
        product.setName(productDTO.getName());
        product.setCategory(categoryService.getCategoryById(productDTO.getCategoryId()).get()); // .get() because getCategoryById gives Optional return
        product.setPrice(productDTO.getPrice());
        product.setWeight(productDTO.getWeight());
        product.setDescription(productDTO.getDescription());

        // instead of storing the name of image directly in DB
        // we must store the self-generated UUID (128 bit number, universally unique identifier)
        // if you generate UUID, cannot use it's original name
        String imageUUID;

        if(!file.isEmpty()){
            imageUUID = file.getOriginalFilename(); // give the original file name
            // now we want to save this file in product images, which is in ~/statics/productImages
            Path fileNameAndPath = Paths.get(uploadDir, imageUUID);  // uploadDir stores actual path
            Files.write(fileNameAndPath, file.getBytes());  // we use getBytes for files as it is a binary
        } else{
            imageUUID = imgName; // the else case means that the file is empty, product is being updated
        }
        product.setImageName(imageUUID);
        productService.addProduct(product);

        return "redirect:/admin/products";

    }



    @GetMapping("/admin/product/delete/{id}")
    public String deleteProduct(@PathVariable long id){
        productService.removeProductBYId(id);
        return "redirect:/admin/products";
    }

    @GetMapping("/admin/product/update/{id}")
    public String updateProduct(@PathVariable long id, Model model){
        Product product = productService.getProductById(id).get();
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setCategoryId(product.getCategory().getId()); // as we get Category object so we fetch id uding getId()
        productDTO.setPrice(product.getPrice());
        productDTO.setWeight(product.getWeight());
        productDTO.setDescription(product.getDescription());
        productDTO.setImageName(product.getImageName());

        model.addAttribute("categories", categoryService.showAllCategories());
        model.addAttribute("productDTO", productDTO);

        return "productsAdd";
    }

}
