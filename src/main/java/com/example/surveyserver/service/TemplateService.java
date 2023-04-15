package com.example.surveyserver.service;

import com.example.surveyserver.model.Option;
import com.example.surveyserver.model.Question;
import com.example.surveyserver.model.Template;
import com.example.surveyserver.repository.OptionRepository;
import com.example.surveyserver.repository.QuestionRepository;
import com.example.surveyserver.repository.TemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TemplateService {

    @Autowired
    private TemplateRepository templateRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private OptionRepository optionRepository;

    public Template createTemplate(Template template) {
        Template savedTemplate = templateRepository.save(template);
        List<Question> questions = template.getQuestions();
        questions.forEach(question -> {
            // bidirectional association to reduce sql statements
            // https://vladmihalcea.com/the-best-way-to-map-a-onetomany-association-with-jpa-and-hibernate/
            question.setTemplate(savedTemplate);
            questionRepository.save(question);
            List<Option> options = question.getOptions();
            if (options != null) {
                options.forEach(option -> {
                    option.setQuestion(question);
                    optionRepository.save(option);
                });
            }
        });
        return savedTemplate;
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
