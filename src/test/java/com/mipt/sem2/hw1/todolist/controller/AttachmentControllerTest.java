package com.mipt.sem2.hw1.todolist.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mipt.sem2.hw1.todolist.dto.AttachmentResponseDto;
import com.mipt.sem2.hw1.todolist.model.TaskAttachment;
import com.mipt.sem2.hw1.todolist.service.AttachmentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AttachmentController.class)
class AttachmentControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private AttachmentService attachmentService;

  @Test
  void uploadAttachment_validFile_returnsCreated() throws Exception {
    UUID taskId = UUID.randomUUID();
    MockMultipartFile file = new MockMultipartFile(
        "file",
        "test.txt",
        MediaType.TEXT_PLAIN_VALUE,
        "Hello, World!".getBytes()
    );

    TaskAttachment saved = new TaskAttachment();
    saved.setId(1L);
    saved.setTaskId(taskId);
    saved.setFileName("test.txt");
    saved.setStoredFileName("uuid_test.txt");
    saved.setContentType(MediaType.TEXT_PLAIN_VALUE);
    saved.setSize(file.getSize());
    saved.setUploadedAt(LocalDateTime.now());

    when(attachmentService.storeAttachment(eq(taskId), any(MockMultipartFile.class)))
        .thenReturn(saved);

    mockMvc.perform(multipart("/api/tasks/{taskId}/attachments", taskId)
            .file(file))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(1L))
        .andExpect(jsonPath("$.fileName").value("test.txt"))
        .andExpect(jsonPath("$.size").value(file.getSize()));
  }

  @Test
  void uploadAttachment_emptyFile_returnsBadRequest() throws Exception {
    UUID taskId = UUID.randomUUID();
    MockMultipartFile emptyFile = new MockMultipartFile(
        "file",
        "empty.txt",
        MediaType.TEXT_PLAIN_VALUE,
        new byte[0]
    );

    when(attachmentService.storeAttachment(eq(taskId), any(MockMultipartFile.class)))
        .thenThrow(new IllegalArgumentException("Cannot store empty file"));

    mockMvc.perform(multipart("/api/tasks/{taskId}/attachments", taskId)
            .file(emptyFile))
        .andExpect(status().isBadRequest());
  }

  @Test
  void downloadAttachment_existing_returnsFile() throws Exception {
    Long attachmentId = 1L;
    Resource resource = new ByteArrayResource("file content".getBytes());
    TaskAttachment attachment = new TaskAttachment();
    attachment.setId(attachmentId);
    attachment.setFileName("test.txt");
    attachment.setContentType(MediaType.TEXT_PLAIN_VALUE);

    when(attachmentService.getAttachment(attachmentId)).thenReturn(Optional.of(attachment));
    when(attachmentService.loadAsResource(attachmentId)).thenReturn(resource);

    mockMvc.perform(get("/api/attachments/{attachmentId}", attachmentId))
        .andExpect(status().isOk())
        .andExpect(header().string("Content-Disposition", "attachment; filename=\"test.txt\""))
        .andExpect(content().bytes("file content".getBytes()));
  }

  @Test
  void downloadAttachment_notFound_returns404() throws Exception {
    Long attachmentId = 999L;

    when(attachmentService.getAttachment(attachmentId)).thenReturn(Optional.empty());

    mockMvc.perform(get("/api/attachments/{attachmentId}", attachmentId))
        .andExpect(status().isNotFound());
  }

  @Test
  void deleteAttachment_existing_returnsNoContent() throws Exception {
    Long attachmentId = 1L;

    doNothing().when(attachmentService).deleteAttachment(attachmentId);

    mockMvc.perform(delete("/api/attachments/{attachmentId}", attachmentId))
        .andExpect(status().isNoContent());
  }

  @Test
  void deleteAttachment_notFound_returnsNotFound() throws Exception {
    Long attachmentId = 999L;

    doThrow(new com.mipt.sem2.hw1.todolist.exception.AttachmentNotFoundException(attachmentId))
        .when(attachmentService).deleteAttachment(attachmentId);

    mockMvc.perform(delete("/api/attachments/{attachmentId}", attachmentId))
        .andExpect(status().isNotFound());
  }

  @Test
  void getTaskAttachments_existingTask_returnsList() throws Exception {
    UUID taskId = UUID.randomUUID();
    TaskAttachment attachment1 = new TaskAttachment();
    attachment1.setId(1L);
    attachment1.setFileName("file1.txt");
    attachment1.setSize(100L);
    attachment1.setUploadedAt(LocalDateTime.now());

    TaskAttachment attachment2 = new TaskAttachment();
    attachment2.setId(2L);
    attachment2.setFileName("file2.txt");
    attachment2.setSize(200L);
    attachment2.setUploadedAt(LocalDateTime.now());

    when(attachmentService.getAttachmentsByTaskId(taskId)).thenReturn(List.of(attachment1, attachment2));

    mockMvc.perform(get("/api/tasks/{taskId}/attachments", taskId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(2))
        .andExpect(jsonPath("$[0].id").value(1L))
        .andExpect(jsonPath("$[1].id").value(2L));
  }
}