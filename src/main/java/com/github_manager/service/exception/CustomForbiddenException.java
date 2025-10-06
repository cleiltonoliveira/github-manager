package com.github_manager.service.exception;

public class CustomForbiddenException extends RuntimeException {

    public CustomForbiddenException(String message) {
        super(message);
    }
}