package com.janseva.backend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Document(collection = "categories")
public class Category {

    @Id
    private String id;

    // Multi-language category names
    private Map<String, String> names;

    // English fallback name
    private String mainCategory;

    private List<String> subCategories;

    // ===== Constructors =====

    public Category() {
    }

    public Category(String id,
                    Map<String, String> names,
                    String mainCategory,
                    List<String> subCategories) {
        this.id = id;
        this.names = names;
        this.mainCategory = mainCategory;
        this.subCategories = subCategories;
    }

    // ===== Getters & Setters =====

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, String> getNames() {
        return names;
    }

    public void setNames(Map<String, String> names) {
        this.names = names;
    }

    public String getMainCategory() {
        return mainCategory;
    }

    public void setMainCategory(String mainCategory) {
        this.mainCategory = mainCategory;
    }

    public List<String> getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(List<String> subCategories) {
        this.subCategories = subCategories;
    }
}