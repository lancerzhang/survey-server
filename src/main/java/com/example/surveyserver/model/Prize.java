package com.example.surveyserver.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Getter
@Setter
@Entity
public class Prize {
    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "survey_id", nullable = false)
    private Survey survey;
    @Column(nullable = false)
    private String name;
    private String description;
    @Column(nullable = false)
    private Integer quantity;
    @Column(updatable = false)
    private Date createdAt;
    private Date lastModified;

    @PrePersist
    public void prePersist() {
        this.createdAt = new Date();
        this.lastModified = new Date();
    }
}
