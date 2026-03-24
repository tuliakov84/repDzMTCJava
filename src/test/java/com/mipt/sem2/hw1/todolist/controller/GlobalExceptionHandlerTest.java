package com.mipt.sem2.hw1.todolist.controller;

import com.mipt.sem2.hw1.todolist.controller.TaskController;
import com.mipt.sem2.hw1.todolist.dto.ErrorResponse;
import com.mipt.sem2.hw1.todolist.dto.TaskCreateDto;
import com.mipt.sem2.hw1.todolist.dto.TaskUpdateDto;
import com.mipt.sem2.hw1.todolist.mapper.TaskMapper;
import com.mipt.sem2.hw1.todolist.service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskService taskService;

    @MockitoBean
    private TaskMapper taskMapper;

    @Test
    void createTask_missingTitle_returnsValidationError() throws Exception {
        String invalidJson = "{\"priority\":\"HIGH\"}";

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.details.title").exists());
    }

    @Test
    void createTask_invalidPriority_returnsValidationError() throws Exception {
        String invalidJson = "{\"title\":\"Task\", \"priority\":\"INVALID\"}";

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Malformed JSON request"));
    }

    @Test
    void updateTask_missingBody_returnsBadRequest() throws Exception {
        mockMvc.perform(put("/api/tasks/123e4567-e89b-12d3-a456-426614174000")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void nonExistentEndpoint_returnsNotFound() throws Exception {
        mockMvc.perform(get("/api/non-existent"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Not Found"));
    }
}