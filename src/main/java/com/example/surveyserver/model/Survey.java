package com.example.surveyserver.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@Table(name = "surveys", indexes = {
        @Index(name = "idx_surveys_user_id", columnList = "user_id"),
        @Index(name = "idx_surveys_template_deleted", columnList = "is_template,is_deleted")
})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Survey {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(nullable = false)
    @Size(max = 255)
    private String title;

    @Size(max = 8000)
    private String description;

    private Boolean isAnonymous;

    private Boolean allowResubmit;

    private Timestamp startTime;

    private Timestamp endTime;

    private Integer maxReplies;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    @Column(name = "is_template", nullable = false)
    private Boolean isTemplate;

    @Column(updatable = false)
    private Date createdAt;

    private Date lastModified;

    @OneToMany(mappedBy = "survey", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Question> questions;

    @OneToMany(mappedBy = "survey", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Subpage> subpages;

    @PrePersist
    public void prePersist() {
        this.isAnonymous = false;
        this.allowResubmit = false;
        this.isDeleted = false;
        this.createdAt = new Date();
        this.lastModified = new Date();
    }

}
