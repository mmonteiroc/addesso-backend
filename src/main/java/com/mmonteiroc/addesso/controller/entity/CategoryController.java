package com.mmonteiroc.addesso.controller.entity;

import com.mmonteiroc.addesso.entity.Category;
import com.mmonteiroc.addesso.exceptions.entity.CategoryNotFoundException;
import com.mmonteiroc.addesso.exceptions.petition.NotRecivedRequiredParamsException;
import com.mmonteiroc.addesso.manager.entity.CategoryManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import javax.websocket.server.PathParam;
import java.io.IOException;
import java.util.Set;

/**
 * Code created by: mmonteiroc
 * Email: miguelmonteiroclaveri@gmail.com
 * Github: https://github.com/mmonteiroc
 * LinkedIn: https://www.linkedin.com/in/mmonteiroc/?locale=en_US
 * Date of creation: 07/06/2020
 * Package:com.mmonteiroc.addesso.controller.entity
 * Project: addesso
 */

@RestController
public class CategoryController {

    @Autowired
    private CategoryManager categoryManager;


    /*
     * --------------
     *
     *  GET MAPPINGS
     *
     * --------------
     * */
    @GetMapping("/category")
    public Set<Category> getAllCategories() {
        return this.categoryManager.findAll();
    }

    @GetMapping("/category/{id}")
    public Category getAllCategories(@PathParam("id") Long id, HttpServletResponse response) throws IOException {
        try {
            return this.categoryManager.findById(id);
        } catch (CategoryNotFoundException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
            return null;
        }
    }

    /*
     * ---------------
     *
     *  POST MAPPINGS
     *
     * ---------------
     * */
    @PostMapping("/category")
    @Transactional
    public ResponseEntity<String> create(@RequestBody String json) {
        try {
            Category category = this.categoryManager.createFromJson(json, true);
            if (category.getIdCategory() != null)
                return new ResponseEntity<>("Id param here not accepted", HttpStatus.BAD_REQUEST);

            this.categoryManager.createOrUpdate(category);
            return new ResponseEntity<>("Category created correctly", HttpStatus.OK);
        } catch (NotRecivedRequiredParamsException e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    /*
     * --------------
     *
     *  PUT MAPPINGS
     *
     * --------------
     * */
    @PutMapping("/category")
    @Transactional
    public ResponseEntity<String> update(@RequestBody String json) {
        Category category = this.categoryManager.createFromJson(json);
        if (category.getIdCategory() == null)
            return new ResponseEntity<>("Id recived not existing on DDBB", HttpStatus.BAD_REQUEST);

        this.categoryManager.createOrUpdate(category);
        return new ResponseEntity<>("Category updated correctly", HttpStatus.OK);
    }

    /*
     * ------------------
     *
     *  DELETE MAPPINGS
     *
     * ------------------
     * */
    @DeleteMapping("/category")
    @Transactional
    public ResponseEntity<String> delete(@RequestBody String json) {
        Category category = this.categoryManager.createFromJson(json);
        if (category.getIdCategory() == null)
            return new ResponseEntity<>("Id recived not existing on DDBB", HttpStatus.BAD_REQUEST);
        if (category.getTickets() != null && category.getTickets().size() > 0)
            return new ResponseEntity<>("CANNOT DELETE: category has tickets assigned", HttpStatus.BAD_REQUEST);

        this.categoryManager.remove(category);
        return new ResponseEntity<>("Category deleted correctly", HttpStatus.OK);
    }
}
