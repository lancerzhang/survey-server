package com.example.surveyserver.service;

import com.example.surveyserver.model.Option;
import com.example.surveyserver.model.Question;
import com.example.surveyserver.model.Template;
import com.example.surveyserver.repository.OptionRepository;
import com.example.surveyserver.repository.QuestionRepository;
import com.example.surveyserver.repository.TemplateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TemplateServiceTest {

    @InjectMocks
    private TemplateService templateService;

    @Mock
    private TemplateRepository templateRepository;

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private OptionRepository optionRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void createTemplate_ShouldPersistTemplateWithQuestionsAndOptions() {
        // Create test data
        Template template = new Template();
        template.setId(1);
        template.setTitle("Test Template");
        template.setDescription("This is a test template");

        Question question1 = new Question();
        question1.setId(2);
        question1.setQuestionText("Question 1");

        Option option1 = new Option();
        option1.setId(3);
        option1.setOptionText("Option 1");

        question1.setOptions(Arrays.asList(option1));
        template.setQuestions(Arrays.asList(question1));

        // Set up mock repositories
        when(templateRepository.save(template)).thenReturn(template);
        when(questionRepository.save(question1)).thenReturn(question1);
        when(optionRepository.save(option1)).thenReturn(option1);

        // Call method under test
        Template savedTemplate = templateService.createTemplate(template);

        // Verify that the template and its associated questions and options were persisted
        verify(templateRepository).save(template);
        verify(questionRepository).save(question1);
        verify(optionRepository).save(option1);

        assertThat(savedTemplate).isNotNull();
        assertThat(savedTemplate.getId()).isNotNull();
        assertThat(savedTemplate.getTitle()).isEqualTo(template.getTitle());
        assertThat(savedTemplate.getDescription()).isEqualTo(template.getDescription());

        List<Question> savedQuestions = savedTemplate.getQuestions();
        assertThat(savedQuestions).hasSize(1);

        Question savedQuestion1 = savedQuestions.get(0);
        assertThat(savedQuestion1.getId()).isNotNull();
        assertThat(savedQuestion1.getQuestionText()).isEqualTo(question1.getQuestionText());

        List<Option> savedOptions = savedQuestion1.getOptions();
        assertThat(savedOptions).hasSize(1);

        Option savedOption1 = savedOptions.get(0);
        assertThat(savedOption1.getId()).isNotNull();
        assertThat(savedOption1.getOptionText()).isEqualTo(option1.getOptionText());
    }

    @Test
    public void getTemplate_ShouldReturnTemplate() {
        // Create test data
        Template template = new Template();
        template.setId(1);
        template.setTitle("Test Template");
        template.setDescription("This is a test template");

        // Set up mock repository
        when(templateRepository.findById(1)).thenReturn(Optional.of(template));

        // Call method under test
        Template foundTemplate = templateService.getTemplate(1);

        // Verify that the correct template was returned
        verify(templateRepository).findById(1);
        assertThat(foundTemplate).isNotNull();
        assertThat(foundTemplate.getId()).isEqualTo(template.getId());
        assertThat(foundTemplate.getTitle()).isEqualTo(template.getTitle());
        assertThat(foundTemplate.getDescription()).isEqualTo(template.getDescription());
    }

    @Test
    public void getTemplate_ShouldReturnNullIfTemplateNotFound() {
        // Set up mock repository
        when(templateRepository.findById(1)).thenReturn(Optional.empty());

        // Call method under test
        Template foundTemplate = templateService.getTemplate(1);

        // Verify that null was returned
        verify(templateRepository).findById(1);
        assertNull(foundTemplate);
    }
}