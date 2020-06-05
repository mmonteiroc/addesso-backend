package com.mmonteiroc.addesso.repository;

import com.mmonteiroc.addesso.entity.Comment;
import com.mmonteiroc.addesso.entity.CommentId;
import org.springframework.data.repository.CrudRepository;

/**
 * Code created by: mmonteiroc
 * Email: miguelmonteiroclaveri@gmail.com
 * Github: https://github.com/mmonteiroc
 * LinkedIn: https://www.linkedin.com/in/mmonteiroc/?locale=en_US
 * Date of creation: 05/06/2020
 * Package: com.mmonteiroc.addesso.repository
 * Project: addesso
 */
public interface CommentRepository extends CrudRepository<Comment, CommentId> {
}