package com.example.surveyserver.model;

import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Entity
@Table(name = "survey_access")
@Data
@NoArgsConstructor
public class SurveyAccess {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer surveyId;

    private Integer userId;

    private Date grantedAt;

}
