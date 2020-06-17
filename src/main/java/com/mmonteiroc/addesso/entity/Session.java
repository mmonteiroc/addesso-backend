package com.mmonteiroc.addesso.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Code created by: mmonteiroc
 * Email: miguelmonteiroclaveri@gmail.com
 * Github: https://github.com/mmonteiroc
 * LinkedIn: https://www.linkedin.com/in/mmonteiroc/?locale=en_US
 * Date of creation: 17/06/2020
 * Package:com.mmonteiroc.addesso.entity
 * Project: addesso
 */
@Entity
@Table(name = "session")
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idsession")
    private Long idSession;

    @Column(name = "last_connect_ip")
    private String lastConnectionIp;

    @Column(name = "last_connection")
    private LocalDateTime lastConnection;

    @Column(name = "browser")
    private String browser;

    @ManyToOne
    @JoinColumn(name = "user_iduser")
    @JsonBackReference
    private User userSession;


    public Session() {
    }

    @PrePersist
    public void updateLastConnection() {
        this.lastConnection = LocalDateTime.now();
    }

    public Long getIdSession() {
        return idSession;
    }

    public void setIdSession(Long idSession) {
        this.idSession = idSession;
    }

    public String getLastConnectionIp() {
        return lastConnectionIp;
    }

    public void setLastConnectionIp(String lastConnectionIp) {
        this.lastConnectionIp = lastConnectionIp;
    }

    public LocalDateTime getLastConnection() {
        return lastConnection;
    }

    public void setLastConnection(LocalDateTime lastConnection) {
        this.lastConnection = lastConnection;
    }

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public User getUserSession() {
        return userSession;
    }

    public void setUserSession(User userSession) {
        this.userSession = userSession;
    }
}
