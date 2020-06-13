package com.mmonteiroc.addesso.manager.entity;

import com.mmonteiroc.addesso.entity.Ticket;
import com.mmonteiroc.addesso.entity.UploadedFile;
import com.mmonteiroc.addesso.exceptions.entity.UploadedFileNotFoundException;
import com.mmonteiroc.addesso.repository.UploadedFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Set;

/**
 * Code created by: mmonteiroc
 * Email: miguelmonteiroclaveri@gmail.com
 * Github: https://github.com/mmonteiroc
 * LinkedIn: https://www.linkedin.com/in/mmonteiroc/?locale=en_US
 * Date of creation: 12/06/2020
 * Package:com.mmonteiroc.addesso.manager.entity
 * Project: addesso
 */
@Service
public class UploadedFileManager {

    @Autowired
    private UploadedFileRepository uploadedFileRepository;

    public void createOrUpdate(UploadedFile... uploadedFiles) {
        Iterable<UploadedFile> iterable = Arrays.asList(uploadedFiles);
        this.uploadedFileRepository.saveAll(iterable);
    }

    public void delete(UploadedFile... uploadedFiles) {
        Iterable<UploadedFile> iterable = Arrays.asList(uploadedFiles);
        this.uploadedFileRepository.deleteAll(iterable);
    }


    public UploadedFile findById(Long id) throws UploadedFileNotFoundException {
        UploadedFile file = this.uploadedFileRepository.findByIdFile(id);
        if (file == null) throw new UploadedFileNotFoundException("File with id [" + id + "] was not found");
        return this.uploadedFileRepository.findByIdFile(id);
    }

    public Set<UploadedFile> findAll() {
        return this.uploadedFileRepository.findAll();
    }

    public Set<UploadedFile> findByTicket(Ticket ticket) {
        return this.uploadedFileRepository.findAllByTicket(ticket);
    }
}
