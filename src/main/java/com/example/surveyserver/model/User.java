package com.example.surveyserver.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "users", indexes = {
        @Index(name = "idx_users_username_staff_id", columnList = "username,staff_id")
})
public class User {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "username", nullable = false)
    @Size(max = 255)
    private String username;

    @Column(name = "staff_id")
    @Size(max = 255)
    private String staffId;

    @Size(max = 1000)
    private String publicKey;

    @Size(max = 255)
    private String email;

    private Date createdAt;

    @Column
    private Date lastModified;

    @PrePersist
    public void prePersist() {
        this.createdAt = new Date();
        this.lastModified = new Date();
    }
}
