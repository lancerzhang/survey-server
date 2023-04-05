package com.example.surveyserver.service;
import com.example.surveyserver.model.Template;
import com.example.surveyserver.model.User;
import com.example.surveyserver.repository.TemplateRepository;
import com.example.surveyserver.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TemplateServiceTest {

    @InjectMocks
    private TemplateService templateService;

    @Mock
    private TemplateRepository templateRepository;

    @Mock
    private UserRepository userRepository;

    @Test
    public void testCreateTemplate() {
        Template template = new Template();
        template.setId(1);
        template.setTitle("Template 1");

        User user = new User();
        user.setId(1);

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(templateRepository.save(template)).thenReturn(template);

        Template createdTemplate = templateService.createTemplate(template, user.getId());

        assertNotNull(createdTemplate);
        assertEquals(template.getId(), createdTemplate.getId());
        assertEquals(template.getTitle(), createdTemplate.getTitle());
        assertEquals(user, createdTemplate.getUser());
        verify(userRepository, times(1)).findById(user.getId());
        verify(templateRepository, times(1)).save(template);
    }

    @Test
    public void testCreateTemplateUserNotFound() {
        Template template = new Template();
        template.setId(1);
        template.setTitle("Template 1");

        when(userRepository.findById(1)).thenReturn(Optional.empty());

        Template result = templateService.createTemplate(template, 1);

        assertNull(result);
        verify(userRepository, times(1)).findById(1);
        verify(templateRepository, times(0)).save(any(Template.class));
    }

    @Test
    public void testGetTemplate() {
        Template template = new Template();
        template.setId(1);
        template.setTitle("Template 1");

        when(templateRepository.findById(template.getId())).thenReturn(Optional.of(template));

        Template fetchedTemplate = templateService.getTemplate(template.getId());

        assertNotNull(fetchedTemplate);
        assertEquals(template.getId(), fetchedTemplate.getId());
        assertEquals(template.getTitle(), fetchedTemplate.getTitle());
        verify(templateRepository, times(1)).findById(template.getId());
    }

    @Test
    public void testUpdateTemplate() {
        Template existingTemplate = new Template();
        existingTemplate.setId(1);
        existingTemplate.setTitle("Template 1");

        Template updatedTemplate = new Template();
        updatedTemplate.setTitle("Updated Template");

        when(templateRepository.findById(existingTemplate.getId())).thenReturn(Optional.of(existingTemplate));
        when(templateRepository.save(existingTemplate)).thenReturn(existingTemplate);

        Template result = templateService.updateTemplate(existingTemplate.getId(), updatedTemplate);

        assertNotNull(result);
        assertEquals(updatedTemplate.getTitle(), result.getTitle());
        verify(templateRepository, times(1)).findById(existingTemplate.getId());
        verify(templateRepository, times(1)).save(existingTemplate);
    }

    @Test
    public void testUpdateTemplateNotFound() {
        Template updatedTemplate = new Template();
        updatedTemplate.setTitle("Updated Template");

        when(templateRepository.findById(1)).thenReturn(Optional.empty());

        Template result = templateService.updateTemplate(1, updatedTemplate);

        assertNull(result);
        verify(templateRepository, times(1)).findById(1);
//        verify(templateRepository, times(0)).save(any(T        // ... previous test methods
    }
        @Test
        public void testGetAllTemplates() {
            Template template1 = new Template();
            template1.setId(1);
            template1.setTitle("Template 1");

            Template template2 = new Template();
            template2.setId(2);
            template2.setTitle("Template 2");

            Page<Template> templatePage = new PageImpl<>(Arrays.asList(template1, template2));
            PageRequest pageable = PageRequest.of(0, 10);

            when(templateRepository.findAllByOrderByCreatedAtDesc(pageable)).thenReturn(templatePage);

            Page<Template> result = templateService.getAllTemplates(pageable);

            assertNotNull(result);
            assertEquals(2, result.getContent().size());
            assertEquals(template1, result.getContent().get(0));
            assertEquals(template2, result.getContent().get(1));
            verify(templateRepository, times(1)).findAllByOrderByCreatedAtDesc(pageable);
        }
    }

