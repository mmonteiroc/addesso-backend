package com.mmonteiroc.addesso.manager.entity;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mmonteiroc.addesso.entity.Category;
import com.mmonteiroc.addesso.exceptions.entity.CategoryNotFoundException;
import com.mmonteiroc.addesso.exceptions.petition.NotRecivedRequiredParamsException;
import com.mmonteiroc.addesso.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Set;

/**
 * Code created by: mmonteiroc
 * Email: miguelmonteiroclaveri@gmail.com
 * Github: https://github.com/mmonteiroc
 * LinkedIn: https://www.linkedin.com/in/mmonteiroc/?locale=en_US
 * Date of creation: 04/06/2020
 * Package: com.mmonteiroc.addesso.manager.entity
 * Project: addesso
 */
@Service
public class CategoryManager {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private Gson gson;

    public Category findById(Long id) throws CategoryNotFoundException {
        Category cat = this.categoryRepository.findByIdCategory(id);
        if (cat == null) throw new CategoryNotFoundException("Category with ID [" + id + "] was not found");
        return cat;
    }

    public Set<Category> findAll() {
        return this.categoryRepository.findAll();
    }


    public Category createFromJson(String json) {
        try {
            return this.createFromJson(json, false);
        } catch (NotRecivedRequiredParamsException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Category createFromJson(String json, boolean requiredParams) throws NotRecivedRequiredParamsException {
        JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
        Category category = new Category();

        if (jsonObject.get("idCategory") != null) {
            try {
                category = this.findById(jsonObject.get("idCategory").getAsLong());
            } catch (CategoryNotFoundException e) {
                e.printStackTrace();
            }
        }

        if (jsonObject.get("name") != null) {
            category.setName(jsonObject.get("name").getAsString());
        } else if (requiredParams) {
            throw new NotRecivedRequiredParamsException("Param name missing");
        }
        if (jsonObject.get("description") != null) {
            category.setDescription(jsonObject.get("description").getAsString());
        } else if (requiredParams) {
            throw new NotRecivedRequiredParamsException("Param description missing");
        }

        return category;
    }

    public void createOrUpdate(Category... categories) {
        Iterable<Category> iterable = Arrays.asList(categories);
        this.categoryRepository.saveAll(iterable);
    }

    public void remove(Category... categories) {
        Iterable<Category> iterable = Arrays.asList(categories);
        this.categoryRepository.deleteAll(iterable);
    }
}
