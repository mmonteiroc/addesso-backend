package com.mmonteiroc.addesso.manager.entity;

import com.mmonteiroc.addesso.entity.Category;
import com.mmonteiroc.addesso.exceptions.entity.CategoryNotFound;
import com.mmonteiroc.addesso.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public Category findById(Long id) throws CategoryNotFound {
        Category cat = this.categoryRepository.findByIdCategory(id);
        if (cat == null) throw new CategoryNotFound("Category with ID [" + id + "] was not found");

        return cat;
    }

}
