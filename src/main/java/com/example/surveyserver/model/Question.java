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
@Table(name = "questions")
public class Question {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(nullable = false)
    private Integer displayOrder;

    @Column(nullable = false)
    @Size(max = 255)
    private String questionType;

    @Column(nullable = false)
    @Size(max = 4000)
    private String questionText;

    private Boolean isMandatory;

    private Integer minSelection;

    private Integer maxSelection;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Option> options;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_id")
    @JsonIgnore
    private Survey survey;

    public enum QuestionType {
        TEXT,
        CHOICE
    }
}
