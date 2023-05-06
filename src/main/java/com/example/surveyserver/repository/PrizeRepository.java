package com.example.surveyserver.repository;

import com.example.surveyserver.model.Prize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrizeRepository extends JpaRepository<Prize, Integer> {
    List<Prize> findBySurveyId(Integer surveyId);
}
