package com.example.surveyserver.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
public class Survey {
    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    private String title;
    private String description;
    private Boolean allowAnonymousReply;
    private Boolean allowResubmit;
    private Timestamp startTime;
    private Timestamp endTime;
    private Integer maxReplies;
    private Boolean isDeleted;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "survey_id")
    private List<Question> questions;
    @Column(nullable = false, updatable = false)
    private Date createdAt;
    @Column(nullable = false)
    private Date lastModified;

    @PrePersist
    public void prePersist() {
        this.createdAt = new Date();
        this.lastModified = new Date();
    }

}
