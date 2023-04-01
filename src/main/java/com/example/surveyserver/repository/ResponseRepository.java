package com.example.surveyserver.repository;

import com.example.surveyserver.model.Response;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.lang.Integer;

@Repository
public interface ResponseRepository extends JpaRepository<Response, Integer> {
}
