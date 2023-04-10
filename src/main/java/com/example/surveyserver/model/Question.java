package com.example.surveyserver.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@Entity
public class Question {
    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subpage_id")
    @JsonIgnore
    private Subpage subpage;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_id")
    @JsonIgnore
    private Survey survey;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id")
    @JsonIgnore
    private Template template;
    @Column(nullable = false)
    private Integer seq;
    @Column(nullable = false)
    @Size(max = 255)
    private String questionType;
    @Column(nullable = false)
    @Size(max = 4000)
    private String questionText;

    public enum QuestionType {
        TEXT,
        RADIO,
        CHECKBOX,
        TEXTAREA
    }

    private Boolean isMandatory;
    private Integer minSelection;
    private Integer maxSelection;
    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "question_id")
    private List<Option> options;

    @PrePersist
    public void prePersist() {
        this.isMandatory = false;
    }
}
