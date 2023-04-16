package com.example.surveyserver.repository;

import com.example.surveyserver.model.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OptionRepository extends JpaRepository<Option, Integer> {
    List<Option> findByQuestionIdIn(List<Integer> questionIds);
}
