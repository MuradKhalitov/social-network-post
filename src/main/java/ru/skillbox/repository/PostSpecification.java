package ru.skillbox.repository;

import org.springframework.data.jpa.domain.Specification;
import ru.skillbox.dto.post.request.PostSearchDto;
import ru.skillbox.model.Post;

import jakarta.persistence.criteria.*;

import java.util.ArrayList;
import java.util.List;

public class PostSpecification {

    public static Specification<Post> filterBySearchDto(PostSearchDto postSearchDto) {
        return (Root<Post> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Фильтрация по id постов
            if (postSearchDto.getIds() != null && !postSearchDto.getIds().isEmpty()) {
                predicates.add(root.get("id").in(postSearchDto.getIds()));
            }

            // Фильтрация по id авторов
            if (postSearchDto.getAccountIds() != null && !postSearchDto.getAccountIds().isEmpty()) {
                predicates.add(root.get("authorId").in(postSearchDto.getAccountIds()));
            }

            // Фильтрация по id заблокированных
            if (postSearchDto.getBlockedIds() != null && !postSearchDto.getBlockedIds().isEmpty()) {
                predicates.add(cb.not(root.get("id").in(postSearchDto.getBlockedIds())));
            }

            // Фильтрация по автору (authorId как строка)
            if (postSearchDto.getAuthor() != null && !postSearchDto.getAuthor().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("author").as(String.class)), "%" + postSearchDto.getAuthor().toLowerCase() + "%"));
            }

            // Фильтрация по заголовку
            if (postSearchDto.getTitle() != null && !postSearchDto.getTitle().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("title")), "%" + postSearchDto.getTitle().toLowerCase() + "%"));
            }

            // Фильтрация по тексту поста
            if (postSearchDto.getPostText() != null && !postSearchDto.getPostText().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("postText")), "%" + postSearchDto.getPostText().toLowerCase() + "%"));
            }

            // Фильтрация по тегам
            if (postSearchDto.getTags() != null && !postSearchDto.getTags().isEmpty()) {
                predicates.add(root.get("tags").in(postSearchDto.getTags()));
            }

            // Фильтрация по дате публикации (диапазон)
            if (postSearchDto.getDateFrom() != null && postSearchDto.getDateTo() != null) {
                predicates.add(cb.between(root.get("publishDate"),
                        cb.literal(postSearchDto.getDateFrom()),
                        cb.literal(postSearchDto.getDateTo())));
            }

            // Фильтрация по статусу удаления
            if (postSearchDto.getIsDeleted() != null) {
                predicates.add(cb.equal(root.get("isDeleted"), postSearchDto.getIsDeleted()));
            }

             //Фильтрация по полю "с друзьями" (withFriends)
//            if (postSearchDto.getWithFriends() != null) {
//                // Предполагается, что в сущности Post есть поле, отражающее связь с друзьями
//                // Например, проверка на автора из списка друзей:
//                if (postSearchDto.getWithFriends()) {
//                    predicates.add(cb.isTrue(root.get("authorId").get("isFriend")));
//                }
//            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
