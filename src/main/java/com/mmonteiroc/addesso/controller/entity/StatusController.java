package com.mmonteiroc.addesso.controller.entity;

import com.mmonteiroc.addesso.entity.Status;
import com.mmonteiroc.addesso.exceptions.entity.StatusNotFoundException;
import com.mmonteiroc.addesso.exceptions.petition.NotRecivedRequiredParamsException;
import com.mmonteiroc.addesso.manager.entity.StatusManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;
import java.io.IOException;
import java.util.Set;

/**
 * Code created by: mmonteiroc
 * Email: miguelmonteiroclaveri@gmail.com
 * Github: https://github.com/mmonteiroc
 * LinkedIn: https://www.linkedin.com/in/mmonteiroc/?locale=en_US
 * Date of creation: 08/06/2020
 * Package:com.mmonteiroc.addesso.controller.entity
 * Project: addesso
 */

@RestController
public class StatusController {

    @Autowired
    private StatusManager statusManager;

    /*
     * --------------
     *
     *  GET MAPPINGS
     *
     * --------------
     * */
    @GetMapping("/status")
    public Set<Status> getall() {
        return this.statusManager.findAll();
    }

    @GetMapping("/status/{id}")
    public Status findById(@PathParam("id") Long id, HttpServletResponse response) throws IOException {
        try {
            return this.statusManager.findById(id);
        } catch (StatusNotFoundException e) {
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

    @PostMapping("/status")
    public ResponseEntity<String> addStatus(@RequestBody String json, HttpServletResponse response) throws IOException {
        try {
            Status status = this.statusManager.convertFromJson(json, true);
            /*
             * In case they send us a id, we dont wanna update cause
             * this endpoint is only to create statuses.
             * So we throw error
             * */
            if (status.getIdStatus() != null)
                return new ResponseEntity<>("WE DONT ACCEPT ID_STATUS HERE", HttpStatus.BAD_REQUEST);

            this.statusManager.createOrUpdate(status);
            return new ResponseEntity<>("Status added correctly", HttpStatus.OK);
        } catch (NotRecivedRequiredParamsException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
            return null;
        }
    }


}
