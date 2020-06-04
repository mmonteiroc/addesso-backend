package com.mmonteiroc.addesso.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.Objects;

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
@Table(name = "badge_user")
@IdClass(BadgeUserId.class)
public class BadgeUser {
    @Id
    @ManyToOne
    @JoinColumn(name = "user_iduser", insertable = false, updatable = false)
    @JsonBackReference
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "badge_idbadge", insertable = false, updatable = false)
    @JsonManagedReference
    private Badge badge;

    @Transient
    private Long idBadge;
    @Transient
    private Long idUser;

    @JsonProperty("user")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @JsonProperty("badge")
    public Badge getBadge() {
        return badge;
    }
    public void setBadge(Badge badge) {
        this.badge = badge;
    }


    @JsonProperty("iduser")
    public Long getIdUser() {
        return user.getIduser();
    }

    @JsonProperty("idbadge")
    public Long getIdBadge() {
        return badge.getIdBadge();
    }

    public void setIdBadge(Long idBadge) {
        this.idBadge = idBadge;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BadgeUser badgeUser = (BadgeUser) o;
        return Objects.equals(user, badgeUser.user) &&
                Objects.equals(badge, badgeUser.badge) &&
                Objects.equals(idBadge, badgeUser.idBadge) &&
                Objects.equals(idUser, badgeUser.idUser);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, badge, idBadge, idUser);
    }
}