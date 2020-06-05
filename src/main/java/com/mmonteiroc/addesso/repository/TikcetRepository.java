package com.mmonteiroc.addesso.repository;

import com.mmonteiroc.addesso.entity.Category;
import com.mmonteiroc.addesso.entity.Ticket;
import com.mmonteiroc.addesso.entity.enums.TicketStatus;
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
public interface TikcetRepository extends CrudRepository<Ticket, Long> {
    Ticket findByIdTicket(Long id);

    Set<Ticket> findAll();

    Set<Ticket> findAllByStatusNotIn(TicketStatus... status);

    Set<Ticket> findAllByStatus(TicketStatus status);

    Set<Ticket> findByCategory(Category cat);
}
