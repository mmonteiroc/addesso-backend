package com.mmonteiroc.addesso.manager.entity;

import com.mmonteiroc.addesso.entity.Ticket;
import com.mmonteiroc.addesso.entity.TicketHistory;
import com.mmonteiroc.addesso.repository.TicketHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Set;

/**
 * Code created by: mmonteiroc
 * Email: miguelmonteiroclaveri@gmail.com
 * Github: https://github.com/mmonteiroc
 * LinkedIn: https://www.linkedin.com/in/mmonteiroc/?locale=en_US
 * Date of creation: 08/06/2020
 * Package:com.mmonteiroc.addesso.manager.entity
 * Project: addesso
 */
@Service
public class TicketHistoryManager {

    @Autowired
    private TicketHistoryRepository ticketHistoryRepository;

    public Set<TicketHistory> findAllByTicket(Ticket ticket) {
        return this.ticketHistoryRepository.findAllByTicketOrderByStatusUpdatedDateDesc(ticket);
    }

    public TicketHistory findById(Long id) {
        return this.ticketHistoryRepository.findByIdHistory(id);
    }

    public void createOrUpdate(TicketHistory... ticketHistories) {
        Iterable<TicketHistory> iterable = Arrays.asList(ticketHistories);
        this.ticketHistoryRepository.saveAll(iterable);
    }

    public void delete(TicketHistory... ticketHistories) {
        Iterable<TicketHistory> iterable = Arrays.asList(ticketHistories);
        this.ticketHistoryRepository.deleteAll(iterable);
    }
}
