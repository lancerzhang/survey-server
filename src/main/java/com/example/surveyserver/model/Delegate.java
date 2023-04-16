package com.example.surveyserver.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name="delegates")
public class Delegate {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(nullable = false)
    private Integer delegator_id;

    @Column(nullable = false)
    private Integer delegate_id;
}
