package com.example.surveyserver.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "prizes")
public class Prize {
    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_id", nullable = false)
    private Survey survey;
    @Column(nullable = false)
    @Size(max = 255)
    private String name;
    @Size(max = 4000)
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
