package com.example.surveyserver.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Date;

@Getter
@Setter
@Entity
public class User {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(nullable = false)
    @Size(max = 255)
    private String username;
    @Column(nullable = false)
    @Size(max = 255)
    private String staffId;
    private Boolean isAnonymous;
    @Size(max = 1000)
    private String publicKey;
    @Size(max = 255)
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
