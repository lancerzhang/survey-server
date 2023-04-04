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

    @ManyToOne
    @JoinColumn(name = "survey_id")
    @JsonIgnore
    private Survey survey;
    @ManyToOne
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
        CHECKBOX
    }

    private Boolean isMandatory;
    private Boolean isMultiline;
    private Integer minSelection;
    private Integer maxSelection;
    @Size(max = 255)
    private String sectionTitle;
    @Size(max = 4000)
    private String sectionDescription;
    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "question_id")
    private List<Option> options;

    @PrePersist
    public void prePersist() {
        this.isMandatory = false;
        this.isMultiline = false;
    }
}
