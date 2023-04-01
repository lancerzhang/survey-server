package com.example.surveyserver.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Survey survey;
    private String questionText;
    private String questionType;

    public enum QuestionType {
        TEXT,
        RADIO,
        CHECKBOX
    }
}
