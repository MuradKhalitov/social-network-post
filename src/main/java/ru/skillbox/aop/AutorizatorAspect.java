package ru.skillbox.aop;

import ru.skillbox.model.User;
import ru.skillbox.service.UserService;
import ru.skillbox.util.CurrentUsers;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class AutorizatorAspect {
    private final UserService userService;

    public AutorizatorAspect(UserService userService) {
        this.userService = userService;
    }

    @Around("@annotation(ru.skillbox.aop.Autorizator)")
    public Object authAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Long updateUserId = null;
        Long oldCommentId = null;
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        if ((methodSignature.getName().equals("getUserById")) || (methodSignature.getName().equals("updateUser"))
                || (methodSignature.getName().equals("deleteUser"))) {
            Object[] arguments = joinPoint.getArgs();
            for (Object arg : arguments) {
                if (arg instanceof Long) {
                    updateUserId = (Long) arg;
                }
            }
            if (updateUserId != null) {
                String currentUsername = CurrentUsers.getCurrentUsername();
                User currentUser = userService.findByUsername(currentUsername);
                User updateUser = userService.getUserById(updateUserId);
                log.info("Пользователь: {}, делает попытку отредактировать или удалить Пользователя: {}"
                        , currentUsername, updateUser.getUsername());
                if (currentUser.getId().equals(updateUserId) || CurrentUsers.hasRole("ADMIN") || CurrentUsers.hasRole("MODERATOR")) {
                    log.info("У пользователя: {}, все получилось!", currentUsername);
                } else {
                    log.info("У {}, недостаточно прав для редактирования или удаления!", currentUsername);
                    return new ResponseEntity<>(HttpStatus.FORBIDDEN);
                }
            }
        }
        Object result = joinPoint.proceed();
        return result;
    }
}

