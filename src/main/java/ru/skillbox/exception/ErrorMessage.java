package ru.skillbox.exception;

public enum ErrorMessage {
    EMPTY_COMMENT_TEXT("Текст комментария не может быть пустым"),
    POST_NOT_FOUND("Пост с идентификатором %s не найден"),
    COMMENT_NOT_FOUND("Комментарий с идентификатором %s не найден");

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String format(Object... args) {
        return String.format(message, args);
    }
}
