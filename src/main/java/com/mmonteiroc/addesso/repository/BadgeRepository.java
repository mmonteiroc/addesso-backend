package com.mmonteiroc.addesso.repository;

import com.mmonteiroc.addesso.entity.Badge;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

/**
 * Code created by: mmonteiroc
 * Email: miguelmonteiroclaveri@gmail.com
 * Github: https://github.com/mmonteiroc
 * LinkedIn: https://www.linkedin.com/in/mmonteiroc/?locale=en_US
 * Date of creation: 04/06/2020
 * Package: com.mmonteiroc.addesso.repository
 * Project: addesso
 */
public interface BadgeRepository extends CrudRepository<Badge, Long> {
    Badge findByIdBadge(Long id);

    Set<Badge> findAll();
}
