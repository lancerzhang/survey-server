package com.example.surveyserver.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class Option {
    @Id
    @GeneratedValue
    private Integer id;

    private String optionText;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;
}
