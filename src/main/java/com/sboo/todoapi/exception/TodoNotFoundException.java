package com.sboo.todoapi.exception;

public class TodoNotFoundException extends RuntimeException {

    public TodoNotFoundException (Long id) {
        super("Not found on this id : " + id);
    }
}
