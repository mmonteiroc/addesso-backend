package com.mmonteiroc.addesso.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.time.LocalDateTime;
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

    @Column(name = "obtained_date", columnDefinition = "DATETIME")
    private LocalDateTime obtainedDate;

    @Transient
    private Long idUser;
    @Transient
    private Long idBadge;

    public BadgeUser() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Badge getBadge() {
        return badge;
    }

    public void setBadge(Badge badge) {
        this.badge = badge;
    }

    public LocalDateTime getObtainedDate() {
        return obtainedDate;
    }

    public void setObtainedDate(LocalDateTime obtainedDate) {
        this.obtainedDate = obtainedDate;
    }

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public Long getIdBadge() {
        return idBadge;
    }

    public void setIdBadge(Long idBadge) {
        this.idBadge = idBadge;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BadgeUser badgeUser = (BadgeUser) o;
        return Objects.equals(user, badgeUser.user) &&
                Objects.equals(badge, badgeUser.badge) &&
                Objects.equals(obtainedDate, badgeUser.obtainedDate) &&
                Objects.equals(idUser, badgeUser.idUser) &&
                Objects.equals(idBadge, badgeUser.idBadge);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, badge, obtainedDate, idUser, idBadge);
    }
}
