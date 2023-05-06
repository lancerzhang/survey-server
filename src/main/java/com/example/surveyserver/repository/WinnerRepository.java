package com.example.surveyserver.repository;

import com.example.surveyserver.model.Winner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WinnerRepository extends JpaRepository<Winner, Integer> {
    List<Winner> findBySurveyId(Integer surveyId);

    List<Winner> findByPrizeId(Integer id);
}
