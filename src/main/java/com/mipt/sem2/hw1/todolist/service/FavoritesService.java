package com.mipt.sem2.hw1.todolist.service;

import com.mipt.sem2.hw1.todolist.dto.TaskResponseDto;
import com.mipt.sem2.hw1.todolist.mapper.TaskMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoritesService {

    private final TaskService taskService;
    private final TaskMapper taskMapper;

    private static final String FAVORITES_SESSION_KEY = "favoriteTaskIds";

    @SuppressWarnings("unchecked")
    private Set<UUID> getFavoritesSet(HttpSession session) {
        Set<UUID> favorites = (Set<UUID>) session.getAttribute(FAVORITES_SESSION_KEY);
        if (favorites == null) {
            favorites = new HashSet<>();
            session.setAttribute(FAVORITES_SESSION_KEY, favorites);
        }
        return favorites;
    }

    public void addFavorite(UUID taskId, HttpSession session) {
        Set<UUID> favorites = getFavoritesSet(session);
        favorites.add(taskId);
        session.setAttribute(FAVORITES_SESSION_KEY, favorites);
    }

    public void removeFavorite(UUID taskId, HttpSession session) {
        Set<UUID> favorites = getFavoritesSet(session);
        favorites.remove(taskId);
        session.setAttribute(FAVORITES_SESSION_KEY, favorites);
    }

    public List<TaskResponseDto> getFavoriteTasks(HttpSession session) {
        Set<UUID> favorites = getFavoritesSet(session);
        return favorites.stream()
                .map(taskService::getTaskById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(taskMapper::toResponseDto)
                .collect(Collectors.toList());
    }
}
