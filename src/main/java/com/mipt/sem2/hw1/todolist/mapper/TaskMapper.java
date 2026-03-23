package com.mipt.sem2.hw1.todolist.mapper;

import com.mipt.sem2.hw1.todolist.dto.TaskCreateDto;
import com.mipt.sem2.hw1.todolist.dto.TaskResponseDto;
import com.mipt.sem2.hw1.todolist.dto.TaskUpdateDto;
import com.mipt.sem2.hw1.todolist.model.Task;
import org.mapstruct.*;
import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    Task toEntity(TaskCreateDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(TaskUpdateDto dto, @MappingTarget Task task);

    TaskResponseDto toResponseDto(Task task);

    @AfterMapping
    default void setCreatedAt(@MappingTarget Task task) {
        if (task.getCreatedAt() == null) {
            task.setCreatedAt(LocalDateTime.now());
        }
    }
}