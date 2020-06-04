package com.mmonteiroc.addesso.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.mmonteiroc.addesso.entity.enums.EstadoTicket;

import javax.persistence.*;

/**
 * Code created by: mmonteiroc
 * Email: miguelmonteiroclaveri@gmail.com
 * Github: https://github.com/mmonteiroc
 * LinkedIn: https://www.linkedin.com/in/mmonteiroc/?locale=en_US
 * Date of creation: 04/06/2020
 * Package: com.mmonteiroc.addesso.entity
 * Project: addesso
 */
@Entity
@Table(name = "ticket")
public class Ticket {

    @Id
    @GeneratedValue
    @Column(name = "idticket")
    private Long idTicket;

    @Column(name = "title")
    private String title;

    @Column(name = "description", length = 400)
    private String description;

    @Column(name = "status", columnDefinition = "tinyint")
    private EstadoTicket status;

    @ManyToOne
    @JoinColumn(name = "user_iduser")
    @JsonManagedReference
    private User user;

    public Ticket() {
    }

    public Long getIdTicket() {
        return idTicket;
    }

    public void setIdTicket(Long idTicket) {
        this.idTicket = idTicket;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public EstadoTicket getStatus() {
        return status;
    }

    public void setStatus(EstadoTicket status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}