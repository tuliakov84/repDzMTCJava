package com.mipt.sem2.hw1.todolist.controller;

import com.mipt.sem2.hw1.todolist.dto.TaskResponseDto;
import com.mipt.sem2.hw1.todolist.service.FavoritesService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FavoritesController.class)
class FavoritesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FavoritesService favoritesService;

    @Test
    void addFavorite_validTaskId_returnsOk() throws Exception {
        UUID taskId = UUID.randomUUID();

        mockMvc.perform(post("/api/favorites/{taskId}", taskId))
                .andExpect(status().isOk());

        verify(favoritesService).addFavorite(eq(taskId), any());
    }

    @Test
    void removeFavorite_validTaskId_returnsNoContent() throws Exception {
        UUID taskId = UUID.randomUUID();

        mockMvc.perform(delete("/api/favorites/{taskId}", taskId))
                .andExpect(status().isNoContent());

        verify(favoritesService).removeFavorite(eq(taskId), any());
    }

    @Test
    void getFavorites_returnsList() throws Exception {
        TaskResponseDto dto1 = new TaskResponseDto();
        dto1.setId(UUID.randomUUID());
        dto1.setTitle("Favorite 1");

        TaskResponseDto dto2 = new TaskResponseDto();
        dto2.setId(UUID.randomUUID());
        dto2.setTitle("Favorite 2");

        when(favoritesService.getFavoriteTasks(any())).thenReturn(List.of(dto1, dto2));

        mockMvc.perform(get("/api/favorites"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value("Favorite 1"))
                .andExpect(jsonPath("$[1].title").value("Favorite 2"));
    }

    @Test
    void getFavorites_emptyList_returnsEmptyArray() throws Exception {
        when(favoritesService.getFavoriteTasks(any())).thenReturn(List.of());

        mockMvc.perform(get("/api/favorites"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }
}