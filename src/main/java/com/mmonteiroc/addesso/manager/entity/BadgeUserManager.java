package com.mmonteiroc.addesso.manager.entity;

import com.mmonteiroc.addesso.entity.BadgeUser;
import com.mmonteiroc.addesso.repository.BadgeUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
public class BadgeUserManager {

    @Autowired
    private BadgeUserRepository badgeUserRepository;

    public Set<BadgeUser> findAll() {
        return this.badgeUserRepository.findAll();
    }
}
