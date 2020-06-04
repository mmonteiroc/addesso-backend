package com.mmonteiroc.addesso.manager.entity;

import com.mmonteiroc.addesso.entity.Category;
import com.mmonteiroc.addesso.entity.Ticket;
import com.mmonteiroc.addesso.repository.TikcetRepository;
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
public class TicketManager {

    @Autowired
    private TikcetRepository tikcetRepository;

    public Ticket findById(Long id) {
        return this.tikcetRepository.findByIdTicket(id);
    }

    public Set<Ticket> findByCategory(Category category) {
        return this.tikcetRepository.findByCategory(category);
    }

    public Set<Ticket> findAll() {
        return this.tikcetRepository.findAll();
    }

    public void createOrUpdate(Ticket... tickets) {
        Iterable<Ticket> iterable = Arrays.asList(tickets);
        this.tikcetRepository.saveAll(iterable);
    }

    public void delete(Ticket... tickets) {
        Iterable<Ticket> iterable = Arrays.asList(tickets);
        this.tikcetRepository.deleteAll(iterable);
    }
}
