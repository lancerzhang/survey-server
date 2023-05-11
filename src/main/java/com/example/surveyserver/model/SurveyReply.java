package com.example.surveyserver.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "survey_replies",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_survey_reply_user_survey", columnNames = {"survey_id", "user_id"})
        },
        indexes = {
                @Index(name = "idx_survey_replies_user_id_created_at", columnList = "user_id,created_at"),
                @Index(name = "idx_survey_replies_survey_id", columnList = "survey_id")
        })
public class SurveyReply {

    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Survey survey;

    @Column(nullable = false)
    private Boolean isAnonymous;

    @Column(name = "created_at", updatable = false)
    private Date createdAt;

    private Date lastModified;

    @OneToMany(mappedBy = "surveyReply", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<QuestionReply> questionReplies;

    @PrePersist
    public void prePersist() {
        this.createdAt = new Date();
        this.lastModified = new Date();
    }
}
