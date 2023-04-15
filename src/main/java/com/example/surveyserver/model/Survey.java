package com.example.surveyserver.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
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

    @Column(nullable = false)
    private Integer userId;

    @Column(nullable = false)
    @Size(max = 255)
    private String title;

    @Size(max = 4000)
    private String description;

    private Boolean allowAnonymousReply;

    private Boolean allowResubmit;

    private Timestamp startTime;

    private Timestamp endTime;

    private Integer maxReplies;

    private Boolean isDeleted;

    @Column(updatable = false)
    private Date createdAt;

    private Date lastModified;

    @OneToMany(mappedBy = "survey", fetch = FetchType.LAZY)
    private List<Question> questions;

    @PrePersist
    public void prePersist() {
        this.allowAnonymousReply = false;
        this.allowResubmit = false;
        this.isDeleted = false;
        this.createdAt = new Date();
        this.lastModified = new Date();
    }

}
