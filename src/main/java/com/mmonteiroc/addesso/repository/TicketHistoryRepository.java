package com.mmonteiroc.addesso.repository;

import com.mmonteiroc.addesso.entity.Ticket;
import com.mmonteiroc.addesso.entity.TicketHistory;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

/**
 * Code created by: mmonteiroc
 * Email: miguelmonteiroclaveri@gmail.com
 * Github: https://github.com/mmonteiroc
 * LinkedIn: https://www.linkedin.com/in/mmonteiroc/?locale=en_US
 * Date of creation: 08/06/2020
 * Package:com.mmonteiroc.addesso.repository
 * Project: addesso
 */
public interface TicketHistoryRepository extends CrudRepository<TicketHistory, Long> {
    TicketHistory findByIdHistory(Long id);

    Set<TicketHistory> findAllByTicket(Ticket ticket);
}
