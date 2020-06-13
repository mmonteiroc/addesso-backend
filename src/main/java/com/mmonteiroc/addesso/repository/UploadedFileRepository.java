package com.mmonteiroc.addesso.repository;

import com.mmonteiroc.addesso.entity.Ticket;
import com.mmonteiroc.addesso.entity.UploadedFile;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

/**
 * Code created by: mmonteiroc
 * Email: miguelmonteiroclaveri@gmail.com
 * Github: https://github.com/mmonteiroc
 * LinkedIn: https://www.linkedin.com/in/mmonteiroc/?locale=en_US
 * Date of creation: 12/06/2020
 * Package:com.mmonteiroc.addesso.repository
 * Project: addesso
 */
public interface UploadedFileRepository extends CrudRepository<UploadedFile, Long> {
    Set<UploadedFile> findAll();

    UploadedFile findByIdFile(Long id);

    Set<UploadedFile> findAllByTicket(Ticket ticket);
}
