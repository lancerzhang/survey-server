package com.example.surveyserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.surveyserver.model.User;
import com.example.surveyserver.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User getUser(Integer id) {
        return userRepository.findById(id).orElse(null);
    }

    public User updateUser(Integer id, User updatedUser) {
        User existingUser = getUser(id);
        if (existingUser == null) {
            return null;
        }
        updatedUser.setId(id);
        return userRepository.save(updatedUser);
    }
}
