package ru.skillbox.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)  // Для методов
@Retention(RetentionPolicy.RUNTIME)  // Сохраняется во время выполнения
public @interface CurrentUserAccount {
}
