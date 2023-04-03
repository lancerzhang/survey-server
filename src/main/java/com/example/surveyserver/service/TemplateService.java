package com.example.surveyserver.service;

import com.example.surveyserver.model.Template;
import com.example.surveyserver.model.User;
import com.example.surveyserver.repository.TemplateRepository;
import com.example.surveyserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TemplateService {

    @Autowired
    private TemplateRepository templateRepository;

    @Autowired
    private UserRepository userRepository;

    public Template createTemplate(Template template, Integer userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return null;
        }
        template.setUser(user);
        return templateRepository.save(template);
    }

    public Template getTemplate(Integer id) {
        return templateRepository.findById(id).orElse(null);
    }

    public Template updateTemplate(Integer id, Template updatedTemplate) {
        Template existingTemplate = templateRepository.findById(id).orElse(null);
        if (existingTemplate == null) {
            return null;
        }
        existingTemplate.setTitle(updatedTemplate.getTitle());
        existingTemplate.setDescription(updatedTemplate.getDescription());
        return templateRepository.save(existingTemplate);
    }

    public Page<Template> getAllTemplates(Pageable pageable) {
        return templateRepository.findAllByOrderByCreatedAtDesc(pageable);
    }
}
