package com.example.surveyserver.controller;

import com.example.surveyserver.model.Template;
import com.example.surveyserver.service.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/templates")
public class TemplateController {

    @Autowired
    private TemplateService templateService;

    @PostMapping
    public Template createTemplate(@Valid @RequestBody Template template) {
        return templateService.createTemplate(template);
    }

    @GetMapping("/{id}")
    public Template getTemplate(@PathVariable Integer id) {
        return templateService.getTemplate(id);
    }

    @PutMapping("/{id}")
    public Template updateTemplate(@PathVariable Integer id, @RequestBody Template template) {
        return templateService.updateTemplate(id, template);
    }

    @GetMapping
    public Page<Template> getAllTemplates(Pageable pageable) {
        return templateService.getAllTemplates(pageable);
    }
}
