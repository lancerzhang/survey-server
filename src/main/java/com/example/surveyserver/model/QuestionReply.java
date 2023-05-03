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
@Table(name = "question_replies")
public class QuestionReply {

    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "surveyReply_id")
    @JsonIgnore
    private SurveyReply surveyReply;

    @Column(nullable = false)
    private Integer questionId;

    @OneToMany(mappedBy = "questionReply", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OptionReply> optionReplies;

    @Size(max = 4000)
    private String replyText;

}