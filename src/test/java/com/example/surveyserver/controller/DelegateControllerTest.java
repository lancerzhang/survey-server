package com.example.surveyserver.controller;

import com.example.surveyserver.model.Delegate;
import com.example.surveyserver.model.User;
import com.example.surveyserver.service.DelegateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class DelegateControllerTest {

    @InjectMocks
    private DelegateController delegateController;

    @Mock
    private DelegateService delegateService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(delegateController).build();
    }

    @Test
    public void testAddDelegate() throws Exception {
        User delegator = new User();
        delegator.setId(1);
        User delegate = new User();
        delegate.setId(2);

        Delegate createdDelegate = new Delegate();
        createdDelegate.setId(1);
        createdDelegate.setDelegator(delegator);
        createdDelegate.setDelegate(delegate);

        when(delegateService.addDelegate(anyInt(), anyInt())).thenReturn(createdDelegate);

        mockMvc.perform(post("/api/delegates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"delegatorId\":1,\"delegateId\":2}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.delegator.id").value(1))
                .andExpect(jsonPath("$.delegate.id").value(2));
    }

    @Test
    public void testRemoveDelegate() throws Exception {
        doNothing().when(delegateService).removeDelegate(anyInt());

        mockMvc.perform(delete("/api/delegates/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
