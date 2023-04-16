package com.example.surveyserver.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "template")
public class Template {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(nullable = false)
    private Integer userId;

    @Column(nullable = false)
    @Size(max = 255)
    private String title;

    @Size(max = 4000)
    private String description;

    private boolean isDeleted;
    @Column(updatable = false)

    private Date createdAt;

    private Date lastModified;

    @OneToMany(mappedBy = "template", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Question> questions;

    @PrePersist
    public void prePersist() {
        this.isDeleted = false;
        this.createdAt = new Date();
        this.lastModified = new Date();
    }
}
