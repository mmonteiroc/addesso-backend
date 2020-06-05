package com.mmonteiroc.addesso.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Code created by: mmonteiroc
 * Email: miguelmonteiroclaveri@gmail.com
 * Github: https://github.com/mmonteiroc
 * LinkedIn: https://www.linkedin.com/in/mmonteiroc/?locale=en_US
 * Date of creation: 05/06/2020
 * Package: com.mmonteiroc.addesso.entity
 * Project: addesso
 */
@Entity
@Table(name = "comment")
@IdClass(CommentId.class)
public class Comment {

    @Id
    @ManyToOne
    @JoinColumn(name = "user_iduser", insertable = false, updatable = false)
    @JsonManagedReference
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "ticket_idticket", insertable = false, updatable = false)
    @JsonBackReference
    private Ticket ticket;

    @Transient
    private Long idUser;
    @Transient
    private Long idTicket;

    @Column(name = "text", length = 400)
    private String text;

    @Column(name = "creation_date", columnDefinition = "DATETIME")
    private LocalDateTime creationDate;


    public Comment() {
    }

    @JsonProperty("iduser")
    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    @JsonProperty("idticket")
    public Long getIdTicket() {
        return idTicket;
    }

    public void setIdTicket(Long idTicket) {
        this.idTicket = idTicket;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }
}
