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
@Table(name = "survey_replies")
public class SurveyReply {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(nullable = false)
    private Integer userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Survey survey;

    @Column(nullable = false)
    private Boolean isAnonymous;

    @Column(updatable = false)
    private Date createdAt;

    private Date lastModified;

    @OneToMany(mappedBy = "surveyReply", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<QuestionReply> questionReplies;

    @PrePersist
    public void prePersist() {
        this.isAnonymous = false;
        this.createdAt = new Date();
        this.lastModified = new Date();
    }
}
