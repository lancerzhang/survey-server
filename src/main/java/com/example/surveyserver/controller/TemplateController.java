package com.example.surveyserver.controller;

import com.example.surveyserver.model.Template;
import com.example.surveyserver.service.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/templates")
public class TemplateController {

    @Autowired
    private TemplateService templateService;

    @PostMapping("/user/{userId}")
    public Template createTemplate(@RequestBody Template template, @PathVariable Integer userId) {
        return templateService.createTemplate(template, userId);
    }

    @GetMapping("/{id}")
    public Template getTemplate(@PathVariable Integer id) {
        return templateService.getTemplate(id);
    }
}
