package ru.skillbox.aop;

import ru.skillbox.mapper.CommentMapper;
import ru.skillbox.mapper.PostMapper;
import ru.skillbox.model.Comment;
import ru.skillbox.model.Post;
import ru.skillbox.service.CommentService;
import ru.skillbox.service.NewsService;
import ru.skillbox.util.CurrentUsers;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;


@Aspect
@Component
@Slf4j
public class AutentificatorAspect {
    private final NewsService newsService;
    private final CommentService commentService;
    private final PostMapper postMapper;
    private final CommentMapper commentMapper;

    public AutentificatorAspect(NewsService service, CommentService commentService, PostMapper postMapper, CommentMapper commentMapper) {
        this.newsService = service;
        this.commentService = commentService;
        this.postMapper = postMapper;
        this.commentMapper = commentMapper;
    }

    @Around("@annotation(ru.skillbox.aop.Autentificator)")
    public Object authAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Long oldNewsId = null;
        Long oldCommentId = null;
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        if ((methodSignature.getName().equals("updateNews")) || (methodSignature.getName().equals("deleteNews"))) {
            Object[] arguments = joinPoint.getArgs();
            for (Object arg : arguments) {
                if (arg instanceof Long) {
                    oldNewsId = (Long) arg;
                }
            }
            if (oldNewsId != null) {
                String currentUsername = CurrentUsers.getCurrentUsername();
                log.info("Пользователь: {}, делает попытку отредактировать или удалить новость", currentUsername);
                Post post = postMapper.convertToEntity(newsService.getNewsById(oldNewsId));
                if (post.getAuthor().getUsername().equals(currentUsername)) {
                    log.info("У пользователя: {}, все получилось!", currentUsername);
                } else {
                    log.info("У {}, недостаточно прав для редактирования или удаления!", currentUsername);
                    return null;
                }
            }
        } else if ((methodSignature.getName().equals("updateComment")) || (methodSignature.getName().equals("deleteComment"))) {
            Object[] arguments = joinPoint.getArgs();
            for (Object arg : arguments) {
                if (arg instanceof Long) {
                    oldCommentId = (Long) arg;
                }
            }
            if (oldCommentId != null) {
                String currentUsername = CurrentUsers.getCurrentUsername();
                log.info("Пользователь: {}, делает попытку отредактировать или удалить комментарий", currentUsername);
                Comment comment = commentMapper.convertToEntity(commentService.getCommentById(oldCommentId));
                if (comment.getAuthor().getUsername().equals(currentUsername)) {
                    log.info("У пользователя: {}, все получилось!", currentUsername);
                } else {
                    log.info("У {}, недостаточно прав для редактирования или удаления!", currentUsername);
                    return null;
                }
            }
        }
        Object result = joinPoint.proceed();
        return result;
    }
}

