package ru.skillbox.aop;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ClearContextAspect {

    @After("@annotation(ru.skillbox.aop.CurrentUserAccount)")  // После выполнения метода
    public void clearCurrentUserContext() {
        CurrentUserContext.clear();  // Очищаем ThreadLocal, чтобы не сохранялись данные между запросами
    }
}
