package com.mmonteiroc.addesso.entity;

import java.io.Serializable;
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
public class BadgeUserId implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long idBAdge;
    private Long idUser;

    public BadgeUserId() {
    }

    public Long getIdBAdge() {
        return idBAdge;
    }

    public void setIdBAdge(Long idBAdge) {
        this.idBAdge = idBAdge;
    }

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BadgeUserId that = (BadgeUserId) o;
        return Objects.equals(idBAdge, that.idBAdge) &&
                Objects.equals(idUser, that.idUser);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idBAdge, idUser);
    }
}