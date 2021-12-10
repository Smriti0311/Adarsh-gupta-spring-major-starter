package com.sheryians.major.repository;

import com.sheryians.major.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;


// JpaRepository provides basic DB methods(save entity, delete, return # of entities, returns entity based on id [select entity from some_table where id=123])
public interface CategoryRepository extends JpaRepository<Category, Integer> {


}
