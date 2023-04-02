package com.example.surveyserver.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "templates")
public class Template {
    
    @Id
    @GeneratedValue
    private Integer id;
    @Column(nullable = false)
    private String title;
    private String description;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    private boolean isDeleted;
    @Column(updatable = false)
    private Date createdAt;
    private Date lastModified;

    @PrePersist
    public void prePersist() {
        this.isDeleted=false;
        this.createdAt = new Date();
        this.lastModified = new Date();
    }
}
