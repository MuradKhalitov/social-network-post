package ru.skillbox.exception;

public class PostNotFoundException extends RuntimeException{
    public PostNotFoundException(String message, Object... args) {
        super(String.format(message.replace("{}", "%s"), args));
    }
}
