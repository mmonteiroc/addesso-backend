package com.mmonteiroc.addesso.controller.entity;

import com.mmonteiroc.addesso.entity.User;
import com.mmonteiroc.addesso.exceptions.petition.NotRecivedRequiredParamsException;
import com.mmonteiroc.addesso.manager.entity.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.Set;

/**
 * Code created by: mmonteiroc
 * Email: miguelmonteiroclaveri@gmail.com
 * Github: https://github.com/mmonteiroc
 * LinkedIn: https://www.linkedin.com/in/mmonteiroc/?locale=en_US
 * Date of creation: 05/06/2020
 * Package: com.mmonteiroc.addesso.controller.entity
 * Project: addesso
 */
@RestController
public class UserController {

    @Autowired
    private UserManager userManager;

    /*
     * --------------
     *
     *  GET MAPPINGS
     *
     * --------------
     * */
    @GetMapping("/user/me")
    public User getMyOwnInf(HttpServletRequest request) {

        return null;
    }

    @GetMapping("/users")
    public Set<User> getAllUsers() {
        return this.userManager.findAll();
    }

    /*
     * ---------------
     *
     *  POST MAPPINGS
     *
     * ---------------
     * */


    /**
     * @param json JSON With data to asing to the user, the JSON expected to be:
     *             {
     * 	            name:'NAME HERE',
     * 	            surname: 'SURNAME HERE',
     * 	            password: 'PASSWD',
     * 	            email: 'EMAIL HERE :D'
     *             }
     * @return
     */
    @PostMapping("/user")
    @Transactional
    public ResponseEntity<String> createUser(@RequestBody String json) {
        try {
            User userToAdd = this.userManager.convertFromJson(json, true);

            /*
             * We get sure they didn't send us any ID_USER
             * */
            if (userToAdd.getIduser() != null)
                return new ResponseEntity<>("We dont accept idUser here", HttpStatus.BAD_REQUEST);

            /*
             * We check the email does not exist
             * in the __DATABASE__ already
             * */
            User posibleEmailUsed = this.userManager.findByEmail(userToAdd.getEmail());
            if (posibleEmailUsed != null)
                return new ResponseEntity<>("Email is registered already", HttpStatus.BAD_REQUEST);

            /*
             * We set default values for roles
             * and we save the user in the database
             * */
            userToAdd.setAdmin(false);
            userToAdd.setTechnician(false);
            this.userManager.create(userToAdd);

            return new ResponseEntity<>("User added correctly", HttpStatus.OK);
        } catch (NotRecivedRequiredParamsException e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
