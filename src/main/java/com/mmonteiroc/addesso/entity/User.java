package com.mmonteiroc.addesso.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
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
@Component
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "iduser")
    private Long iduser;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "passwd")
    private String passwd;

    @Column(name = "email")
    private String email;

    /*
     * Roles of the user
     * */
    @Column(name = "is_admin", columnDefinition = "bit")
    private Boolean isAdmin;

    @Column(name = "is_technician", columnDefinition = "bit")
    private Boolean isTechnician;

    /*
     * This isn't saved, just gives the token of access to the frontend
     * */
    @Transient
    private String accessPhoto;

    @OneToOne
    @JoinColumn(name = "uploadedfile_idfile")
    private UploadedFile profilePhoto;

    @OneToMany(mappedBy = "userAssigned", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private Set<Ticket> ticketsAsigned = new HashSet<>();

    @OneToMany(mappedBy = "userOwner", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private Set<Ticket> ticketsCreated = new HashSet<>();

    @OneToMany(mappedBy = "userAddress", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<Address> addresses = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private Set<Comment> comments = new HashSet<>();


    @OneToMany(mappedBy = "userSession", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<Session> sessions = new HashSet<>();


    public User() {
    }

    public void addComment(Ticket ticket, String text) {
        Comment comment = new Comment();
        comment.setUser(this);
        comment.setTicket(ticket);
        comment.setText(text);
        this.comments.add(comment);
        ticket.getComments().add(comment);
    }

    public void removeComment(Ticket ticket) {
        Comment comment = new Comment();
        comment.setUser(this);
        comment.setTicket(ticket);
        ticket.getComments().remove(comment);
        this.comments.remove(comment);
    }

    public void addSession(Session session) {
        this.sessions.add(session);
    }


    public Long getIduser() {
        return iduser;
    }

    public void setIduser(Long iduser) {
        this.iduser = iduser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    @JsonIgnore
    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }

    public Set<Ticket> getTicketsAsigned() {
        return ticketsAsigned;
    }

    public void setTicketsAsigned(Set<Ticket> ticketsAsigned) {
        this.ticketsAsigned = ticketsAsigned;
    }

    public Set<Ticket> getTicketsCreated() {
        return ticketsCreated;
    }

    public void setTicketsCreated(Set<Ticket> ticketsCreated) {
        this.ticketsCreated = ticketsCreated;
    }

    public UploadedFile getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(UploadedFile profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public Boolean isTechnician() {
        return isTechnician;
    }

    public void setTechnician(Boolean technician) {
        isTechnician = technician;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public String getAccessPhoto() {
        return accessPhoto;
    }

    public void setAccessPhoto(String accessPhoto) {
        this.accessPhoto = accessPhoto;
    }

    public Set<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(Set<Address> addresses) {
        this.addresses = addresses;
    }

    public Boolean getAdmin() {
        return isAdmin;
    }

    public Boolean getTechnician() {
        return isTechnician;
    }

    public Set<Session> getSessions() {
        return sessions;
    }

    public void setSessions(Set<Session> sessions) {
        this.sessions = sessions;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(iduser, user.iduser) &&
                Objects.equals(name, user.name) &&
                Objects.equals(surname, user.surname) &&
                Objects.equals(passwd, user.passwd) &&
                Objects.equals(email, user.email) &&
                Objects.equals(isAdmin, user.isAdmin) &&
                Objects.equals(isTechnician, user.isTechnician) &&
                Objects.equals(accessPhoto, user.accessPhoto) &&
                Objects.equals(profilePhoto, user.profilePhoto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(iduser, name, surname, passwd, email, isAdmin, isTechnician, accessPhoto, profilePhoto);
    }

    @Override
    public String toString() {
        return "User{" +
                "iduser=" + iduser +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", passwd='" + passwd + '\'' +
                ", email='" + email + '\'' +
                ", isAdmin=" + isAdmin +
                ", isTechnician=" + isTechnician +
                ", accessPhoto='" + accessPhoto + '\'' +
                ", profilePhoto=" + profilePhoto +
                ", ticketsAsigned=" + ticketsAsigned +
                ", ticketsCreated=" + ticketsCreated +
                ", addresses=" + addresses +
                ", comments=" + comments +
                ", sessions=" + sessions +
                '}';
    }
}

