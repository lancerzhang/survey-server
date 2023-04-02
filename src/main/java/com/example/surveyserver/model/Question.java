package com.example.surveyserver.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Getter
@Setter
@Entity
public class Question {
    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "survey_id", nullable = false)
    private Survey survey;
    private String questionText;
    private String questionType;

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
}
