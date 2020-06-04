package com.mmonteiroc.addesso.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

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
@Table(name = "badge")
public class Badge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idbadge")
    private Long idBadge;

    @Column(name = "name")
    private String name;

    @Column(name = "description", length = 300)
    private String description;

    @OneToMany(mappedBy = "badge", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private Set<BadgeUser> badgeUserSet = new HashSet<>();


    public Badge() {
    }


    public Long getIdBadge() {
        return idBadge;
    }

    public void setIdBadge(Long idBadge) {
        this.idBadge = idBadge;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public Set<BadgeUser> getBadgeUserSet() {
        return badgeUserSet;
    }

    public void setBadgeUserSet(Set<BadgeUser> badgeUserSet) {
        this.badgeUserSet = badgeUserSet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Badge badge = (Badge) o;
        return Objects.equals(idBadge, badge.idBadge) &&
                Objects.equals(name, badge.name) &&
                Objects.equals(description, badge.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idBadge, name, description);
    }
}
