package com.mmonteiroc.addesso.entity;

import java.io.Serializable;
import java.util.Objects;

/**
 * Code created by: mmonteiroc
 * Email: miguelmonteiroclaveri@gmail.com
 * Github: https://github.com/mmonteiroc
 * LinkedIn: https://www.linkedin.com/in/mmonteiroc/?locale=en_US
 * Date of creation: 05/06/2020
 * Package: com.mmonteiroc.addesso.entity
 * Project: addesso
 */
public class CommentId implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long user;
    private Long ticket;

    public CommentId() {
    }

    public Long getUser() {
        return user;
    }

    public void setUser(Long user) {
        this.user = user;
    }

    public Long getTicket() {
        return ticket;
    }

    public void setTicket(Long ticket) {
        this.ticket = ticket;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommentId commentId = (CommentId) o;
        return Objects.equals(user, commentId.user) &&
                Objects.equals(ticket, commentId.ticket);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, ticket);
    }
}
