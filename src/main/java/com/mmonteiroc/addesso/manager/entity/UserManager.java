package com.mmonteiroc.addesso.manager.entity;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mmonteiroc.addesso.entity.User;
import com.mmonteiroc.addesso.exceptions.petition.NotRecivedRequiredParamsException;
import com.mmonteiroc.addesso.repository.UserRepository;
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
public class UserManager {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Gson gson;


    public User findById(Long id) {
        return this.userRepository.findByIduser(id);
    }

    public User findByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }

    public Set<User> findAll() {
        return this.userRepository.findAll();
    }

    public void create(User... users) {
        /*
         * since the user is going to be created as new,
         * we cypher the passwd before inserting it
         * */
        for (User user : users) {
            user.setPasswd(this.cypherPassword(user.getPasswd()));
        }

        Iterable<User> iterable = Arrays.asList(users);
        this.userRepository.saveAll(iterable);
    }


    /**
     * @param cypherPasswd If its true, we gonna cypher the passwd before we
     *                     update the user. Thats correctly used when the password was changed
     * @param users        Array of users to update;
     */
    public void update(boolean cypherPasswd, User... users) {

        /*
         * We cypher the passwd in case was changed the passwd
         * */
        if (cypherPasswd) {
            for (User user : users) {
                user.setPasswd(this.cypherPassword(user.getPasswd()));
            }
        }

        Iterable<User> iterable = Arrays.asList(users);
        this.userRepository.saveAll(iterable);
    }

    public void delete(User... users) {
        Iterable<User> iterable = Arrays.asList(users);
        this.userRepository.deleteAll(iterable);
    }


    public User convertFromJson(String json) {
        try {
            return this.convertFromJson(json, false);
        } catch (NotRecivedRequiredParamsException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param json          to get the params
     * @param requireParams If true checks that params musn't be missing
     * @return user with param recived
     * @throws NotRecivedRequiredParamsException if there was params required missing
     */
    public User convertFromJson(String json, boolean requireParams) throws NotRecivedRequiredParamsException {
        JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
        User userToReturn = new User();
        if (jsonObject.get("iduser") != null) {
            userToReturn = this.findById(jsonObject.get("iduser").getAsLong());
            if (userToReturn == null) userToReturn = new User();
        }

        if (jsonObject.get("name") != null) {
            userToReturn.setName(jsonObject.get("name").getAsString());
        } else if (requireParams) {
            throw new NotRecivedRequiredParamsException("Param name was required");
        }

        if (jsonObject.get("surname") != null) {
            userToReturn.setSurname(jsonObject.get("surname").getAsString());
        } else if (requireParams) {
            throw new NotRecivedRequiredParamsException("Param surname was required");
        }

        if (jsonObject.get("passwd") != null) {
            userToReturn.setPasswd(jsonObject.get("passwd").getAsString());
        } else if (requireParams) {
            throw new NotRecivedRequiredParamsException("Param passwd was required");
        }

        if (jsonObject.get("email") != null) {
            userToReturn.setEmail(jsonObject.get("email").getAsString());
        } else if (requireParams) {
            throw new NotRecivedRequiredParamsException("Param email was required");
        }

        return userToReturn;
    }

    private String cypherPassword(String passwordWithoutCypher) {
        return BCrypt.withDefaults().hashToString(12, passwordWithoutCypher.toCharArray());
    }

    private boolean validatePassword(User fromLoginNotCypher, User fromDatabaseToValidate) {
        BCrypt.Result result = BCrypt.verifyer().verify(fromLoginNotCypher.getPasswd().toCharArray(), fromDatabaseToValidate.getPasswd());

        return result.verified;
    }
}
