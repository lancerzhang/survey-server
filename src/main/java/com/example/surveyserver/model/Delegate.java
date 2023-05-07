package com.example.surveyserver.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "delegates", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"delegator_id", "delegate_id"})
})
public class Delegate {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "delegator_id", nullable = false)
    private Integer delegatorId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delegate_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private User delegate;
}
