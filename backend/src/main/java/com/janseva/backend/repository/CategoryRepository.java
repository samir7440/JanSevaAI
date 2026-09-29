package com.janseva.backend.repository;

import com.janseva.backend.model.Category;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CategoryRepository extends MongoRepository<Category, String> {
    
    // Fallback: Agar exact match mil jaye
    List<Category> findByMainCategoryContainingIgnoreCase(String query);
}