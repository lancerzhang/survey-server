package com.example.surveyserver.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Getter
@Setter
@Entity
public class Option {
    @Id
    @GeneratedValue
    private Integer id;
    @Column(nullable = false)
    private Integer seq;
    @Column(nullable = false)
    @Size(max = 4000)
    private String optionText;

    @ManyToOne
    @JoinColumn(name = "question_id")
    @JsonIgnore
    private Question question;
}
