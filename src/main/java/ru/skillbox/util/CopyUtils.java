package ru.skillbox.util;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.lang.reflect.Field;

@UtilityClass
public class CopyUtils
{
    @SneakyThrows
    public void copyNonNullProperties(Object source, Object destination) {
        Class<?> aClass = source.getClass();
        Field[] fields = aClass.getDeclaredFields();
        for(Field field: fields) {
            field.setAccessible(true);
            Object value = field.get(source);
            if(value != null) {
                field.set(destination, value);
            }
        }
    }
}
