package com.mmonteiroc.addesso.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Code created by: mmonteiroc
 * Email: miguelmonteiroclaveri@gmail.com
 * Github: https://github.com/mmonteiroc
 * LinkedIn: https://www.linkedin.com/in/mmonteiroc/?locale=en_US
 * Date of creation: 07/06/2020
 * Package:com.mmonteiroc.addesso.entity
 * Project: addesso
 */
@Entity
@Table(name = "ticket_history")
public class TicketHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idhistory")
    private Long idHistory;

    @ManyToOne
    @JoinColumn(name = "status_idstatus")
    @JsonManagedReference
    private Status status;


    @ManyToOne
    @JoinColumn(name = "ticket_idticket")
    @JsonBackReference
    private Ticket ticket;

    @Column(name = "status_updated_date", columnDefinition = "DATETIME")
    private LocalDateTime statusUpdatedDate;

    public TicketHistory() {
    }

    @PrePersist
    public void prePersist() {
        this.statusUpdatedDate = LocalDateTime.now();
    }


    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public LocalDateTime getStatusUpdatedDate() {
        return statusUpdatedDate;
    }

    public void setStatusUpdatedDate(LocalDateTime statusUpdatedDate) {
        this.statusUpdatedDate = statusUpdatedDate;
    }

    public Long getIdHistory() {
        return idHistory;
    }

    public void setIdHistory(Long idHistory) {
        this.idHistory = idHistory;
    }
}
