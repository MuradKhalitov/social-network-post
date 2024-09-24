package ru.skillbox.exception;

public class LikePostNotFoundException extends RuntimeException{
    public LikePostNotFoundException(String message) {
        super(message);
    }
}
