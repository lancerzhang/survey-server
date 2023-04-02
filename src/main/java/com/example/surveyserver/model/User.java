package com.example.surveyserver.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
public class User {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String staffId;
    private Boolean isAnonymous;
    private String publicKey;
    private String email;
    private Date createdAt;
    @Column
    private Date lastModified;

    @PrePersist
    public void prePersist() {
        this.isAnonymous=false;
        this.createdAt = new Date();
        this.lastModified = new Date();
    }
}
