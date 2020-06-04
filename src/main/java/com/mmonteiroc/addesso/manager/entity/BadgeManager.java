package com.mmonteiroc.addesso.manager.entity;

import com.mmonteiroc.addesso.entity.Badge;
import com.mmonteiroc.addesso.repository.BadgeRepository;
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
public class BadgeManager {

    @Autowired
    private BadgeRepository badgeRepository;

    public Badge findById(Long id) {
        return this.badgeRepository.findByIdBadge(id);
    }

    public Set<Badge> findAll() {
        return this.badgeRepository.findAll();
    }

    public void createOrUpdate(Badge... badges) {
        Iterable<Badge> iterable = Arrays.asList(badges);
        this.badgeRepository.saveAll(iterable);
    }

    public void delete(Badge... badges) {
        Iterable<Badge> iterable = Arrays.asList(badges);
        this.badgeRepository.deleteAll(iterable);
    }

}
