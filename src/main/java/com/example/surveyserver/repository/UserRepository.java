package com.example.surveyserver.repository;

import com.example.surveyserver.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("SELECT u FROM User u WHERE LOWER(u.username) LIKE LOWER(CONCAT('%', :searchString, '%')) OR LOWER(u.staffId) LIKE LOWER(CONCAT('%', :searchString, '%'))")
    List<User> searchByUsernameOrStaffId(@Param("searchString") String searchString, Pageable pageable);
}