package com.mmonteiroc.addesso.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idticket")
    private Long idTicket;

    @Column(name = "title")
    private String title;

    @Column(name = "description", length = 400)
    private String description;

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<TicketHistory> ticketHistories;

    @ManyToOne
    @JoinColumn(name = "user_iduser_asigned")
    @JsonManagedReference
    private User userAsigned;

    @ManyToOne
    @JoinColumn(name = "user_iduser_owner", nullable = false)
    @JsonManagedReference
    private User userOwner;

    @ManyToOne
    @JoinColumn(name = "category_idcategory")
    @JsonManagedReference
    private Category category;

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<UploadedFile> atachedFiles = new HashSet<>();

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<Comment> comments = new HashSet<>();


    @Column(name = "created", columnDefinition = "DATETIME")
    private LocalDateTime created;

    @Column(name = "last_update", columnDefinition = "DATETIME")
    private LocalDateTime lastUpdate;

    public Ticket() {
    }

    @PrePersist
    public void onCreated() {
        this.created = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        this.lastUpdate = LocalDateTime.now();
    }

    public void addStatus(Status status) {
        TicketHistory history = new TicketHistory();
        history.setTicket(this);
        history.setStatus(status);
        this.ticketHistories.add(history);
        status.getTicketHistories().add(history);
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

    public User getUserAsigned() {
        return userAsigned;
    }

    public void setUserAsigned(User userAsigned) {
        this.userAsigned = userAsigned;
    }

    public User getUserOwner() {
        return userOwner;
    }

    public void setUserOwner(User userOwner) {
        this.userOwner = userOwner;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Set<UploadedFile> getAtachedFiles() {
        return atachedFiles;
    }

    public void setAtachedFiles(Set<UploadedFile> atachedFiles) {
        this.atachedFiles = atachedFiles;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public Set<TicketHistory> getTicketHistories() {
        return ticketHistories;
    }

    public void setTicketHistories(Set<TicketHistory> ticketHistories) {
        this.ticketHistories = ticketHistories;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}