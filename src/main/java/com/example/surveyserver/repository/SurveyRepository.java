package com.example.surveyserver.repository;

import com.example.surveyserver.model.Survey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SurveyRepository extends JpaRepository<Survey, Integer> {
    Page<Survey> findByUserIdOrderById(Integer userId, Pageable pageable);
}

