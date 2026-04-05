package com.mipt.sem2.hw1.todolist.controller;

import com.mipt.sem2.hw1.todolist.dto.TaskResponseDto;
import com.mipt.sem2.hw1.todolist.service.FavoritesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class FavoritesController {

    private final FavoritesService favoritesService;

    @PostMapping("/{taskId}")
    public ResponseEntity<Void> addFavorite(@PathVariable UUID taskId, HttpSession session) {
        favoritesService.addFavorite(taskId, session);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> removeFavorite(@PathVariable UUID taskId, HttpSession session) {
        favoritesService.removeFavorite(taskId, session);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<TaskResponseDto>> getFavorites(HttpSession session) {
        return ResponseEntity.ok(favoritesService.getFavoriteTasks(session));
    }
}