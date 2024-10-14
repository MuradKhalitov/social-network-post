package ru.skillbox.exception;

public class CommentNotFoundException extends RuntimeException {
    public CommentNotFoundException(String message, Object... args) {
        super(String.format(message.replace("{}", "%s"), args));
    }
}
