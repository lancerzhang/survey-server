package com.example.surveyserver.repository;

import com.example.surveyserver.model.UserPrize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.lang.Integer;

@Repository
public interface UserPrizeRepository extends JpaRepository<UserPrize, Integer> {
}
