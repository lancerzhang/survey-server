package com.example.surveyserver.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
public class Question {
    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "survey_id")
    @JsonIgnore
    private Survey survey;
    @Column(nullable = false)
    private Integer seq;
    @Column(nullable = false)
    private String questionType;
    @Column(nullable = false)
    private String questionText;

    public enum QuestionType {
        TEXT,
        RADIO,
        CHECKBOX
    }
    private Boolean isMandatory;
    private Boolean isMultiline;
    private Integer minSelection;
    private Integer maxSelection;
    private String sectionTitle;
    private String sectionDescription;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "question_id")
    private List<Option> options;

    @PrePersist
    public void prePersist() {
        this.isMandatory=false;
        this.isMultiline=false;
    }
}
