package com.example.surveyserver.repository;

import com.example.surveyserver.model.SurveyAccess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SurveyAccessRepository extends JpaRepository<SurveyAccess, Integer> {
    boolean existsBySurveyIdAndUserId(int surveyId, int userId);
}