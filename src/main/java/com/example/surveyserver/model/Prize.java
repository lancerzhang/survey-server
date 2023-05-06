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
@Table(name = "prizes")
public class Prize {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(nullable = false)
    private Integer surveyId;

    @Column(nullable = false)
    @Size(max = 255)
    private String name;

    @Size(max = 4000)
    private String description;

    @Column(nullable = false)
    private Integer quantity;

    @OneToMany(mappedBy = "prize", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Winner> winners;

    @Column(updatable = false)
    private Date createdAt;

    private Date lastModified;

    @PrePersist
    public void prePersist() {
        this.createdAt = new Date();
        this.lastModified = new Date();
    }
}
