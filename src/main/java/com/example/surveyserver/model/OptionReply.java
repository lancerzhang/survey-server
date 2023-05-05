package com.example.surveyserver.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "option_replies")
public class OptionReply {

    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "questionReply_id")
    @JsonIgnore
    private QuestionReply questionReply;

    @Column(nullable = false)
    private Integer optionId;

    private boolean selected = true;
}