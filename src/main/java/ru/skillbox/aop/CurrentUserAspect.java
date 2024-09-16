package ru.skillbox.aop;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import ru.skillbox.model.Account;
import ru.skillbox.repository.UserRepository;
import ru.skillbox.util.CurrentUsers;

import java.util.UUID;

@Aspect
@Component
@RequiredArgsConstructor
public class CurrentUserAspect {

    private final CurrentUsers currentUsers;
    private final UserRepository userRepository;

    @Before("@annotation(ru.skillbox.aop.CurrentUserAccount)")  // Перед выполнением метода с аннотацией
    public void setCurrentUserAccount() {
        // Извлекаем текущего пользователя
        UUID userId = currentUsers.getCurrentUserId();
        Account currentAccount = userRepository.findById(userId).orElseThrow(
                () -> new RuntimeException("User not found")
        );

        // Сохраняем текущего пользователя в контексте
        CurrentUserContext.setCurrentUser(currentAccount);
    }
}

