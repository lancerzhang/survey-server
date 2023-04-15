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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "surveyReply_id")
    @JsonIgnore
    private SurveyReply surveyReply;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;


    @OneToMany(mappedBy = "questionReply", fetch = FetchType.LAZY)
    private List<OptionReply> optionReplies;

    @Size(max = 4000)
    private String replyText;

}