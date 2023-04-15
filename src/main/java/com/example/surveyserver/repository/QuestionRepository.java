package com.example.surveyserver.repository;

import com.example.surveyserver.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer> {
    List<Question> findBySurveyId(int surveyId);
}
