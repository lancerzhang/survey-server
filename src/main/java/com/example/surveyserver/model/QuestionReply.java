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
public class QuestionReply {

    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "surveyReply_id")
    @JsonIgnore
    private SurveyReply surveyReply;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "questionReply_id")
    private List<OptionReply> optionReplies;

    @Size(max = 4000)
    private String replyText;

}