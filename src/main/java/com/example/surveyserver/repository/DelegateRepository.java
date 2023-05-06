package com.example.surveyserver.repository;

import com.example.surveyserver.model.Delegate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DelegateRepository extends JpaRepository<Delegate, Integer> {

    List<Delegate> findByDelegatorId(Integer delegatorId);
}
