package com.mmonteiroc.addesso.manager.entity;

import com.mmonteiroc.addesso.entity.User;
import com.mmonteiroc.addesso.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
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

    public User findById(Long id) {
        return this.userRepository.findByIduser(id);
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


    private String cypherPassword(String passwordWithoutCypher) {
        return BCrypt.hashpw(passwordWithoutCypher, BCrypt.gensalt());
    }

}
