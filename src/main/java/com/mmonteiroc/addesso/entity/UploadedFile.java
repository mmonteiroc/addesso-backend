package com.mmonteiroc.addesso.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.time.LocalDateTime;

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
@Table(name = "uploaded_file")
public class UploadedFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idfile")
    private Long idFile;

    @Column(name = "name")
    private String name;

    @Column(name = "upload_date", columnDefinition = "DATETIME")
    private LocalDateTime uploadDate;

    @Column(name = "file_type")
    private String filetype;

    @Column(name = "content_type")
    private String contentType;

    @ManyToOne
    @JoinColumn(name = "ticket_idticket")
    @JsonBackReference
    private Ticket ticket;

    public UploadedFile() {
    }

    @PrePersist
    public void setUploaded() {
        this.uploadDate = LocalDateTime.now();
    }

    public Long getIdFile() {
        return idFile;
    }

    public void setIdFile(Long idFile) {
        this.idFile = idFile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(LocalDateTime uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getFiletype() {
        return filetype;
    }

    public void setFiletype(String filetype) {
        this.filetype = filetype;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @Override
    public String toString() {
        return "UploadedFile{" +
                "idFile=" + idFile +
                ", name='" + name + '\'' +
                ", uploadDate=" + uploadDate +
                ", filetype='" + filetype + '\'' +
                ", contentType='" + contentType + '\'' +
                ", ticket=" + ticket +
                '}';
    }
}
