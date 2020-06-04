package com.mmonteiroc.addesso.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.mmonteiroc.addesso.entity.enums.LoginMode;
import net.minidev.json.annotate.JsonIgnore;

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
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue
    @Column(name = "iduser")
    private Long iduser;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "surname")
    private String surname;

    @JsonIgnore
    @Column(name = "passwd")
    private String passwd;

    @Column(name = "email")
    private String email;

    @Column(name = "is_admin", columnDefinition = "bit")
    private boolean isAdmin;

    @Column(name = "login_mode", columnDefinition = "tinyint")
    private LoginMode loginMode;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<BadgeUser> badgeUserSet = new HashSet<>();

    @OneToMany(mappedBy = "userAsigned", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private Set<Ticket> ticketsAsigned;

    @OneToMany(mappedBy = "userOwner", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private Set<Ticket> ticketsCreated;

    public User() {
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

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public LoginMode getLoginMode() {
        return loginMode;
    }

    public void setLoginMode(LoginMode loginMode) {
        this.loginMode = loginMode;
    }

    public Set<BadgeUser> getBadgeUserSet() {
        return badgeUserSet;
    }

    public void setBadgeUserSet(Set<BadgeUser> badgeUserSet) {
        this.badgeUserSet = badgeUserSet;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return isAdmin == user.isAdmin &&
                Objects.equals(iduser, user.iduser) &&
                Objects.equals(name, user.name) &&
                Objects.equals(surname, user.surname) &&
                Objects.equals(passwd, user.passwd) &&
                Objects.equals(email, user.email) &&
                loginMode == user.loginMode;
    }

    @Override
    public int hashCode() {
        return Objects.hash(iduser, name, surname, passwd, email, isAdmin, loginMode);
    }
}

