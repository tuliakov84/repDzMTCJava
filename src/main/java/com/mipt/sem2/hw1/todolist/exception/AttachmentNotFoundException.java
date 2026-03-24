package com.mipt.sem2.hw1.todolist.exception;

public class AttachmentNotFoundException extends RuntimeException {
    public AttachmentNotFoundException(Long id) {
        super("Attachment not found with id: " + id);
    }
}