package com.example.surveyserver.controller;

import com.example.surveyserver.model.User;
import com.example.surveyserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public User getUser(@PathVariable Integer id) {
        return userService.getUser(id);
    }

    @GetMapping("/search")
    public List<User> searchUsers(@RequestParam("searchString") String searchString) {
        return userService.searchUsers(searchString);
    }

}
