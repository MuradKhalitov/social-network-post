package ru.skillbox.repository;

import org.springframework.data.jpa.domain.Specification;
import ru.skillbox.dto.SearchDto;
import ru.skillbox.model.Post;

import jakarta.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

public class PostSpecification {

    public static Specification<Post> filterBySearchDto(SearchDto searchDto) {
        return (Root<Post> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Фильтрация по id постов
            if (searchDto.getIds() != null && !searchDto.getIds().isEmpty()) {
                predicates.add(root.get("id").in(searchDto.getIds()));
            }

            // Фильтрация по id авторов
            if (searchDto.getAccountIds() != null && !searchDto.getAccountIds().isEmpty()) {
                predicates.add(root.get("authorId").in(searchDto.getAccountIds()));
            }

            // Фильтрация по id заблокированных
            if (searchDto.getBlockedIds() != null && !searchDto.getBlockedIds().isEmpty()) {
                predicates.add(cb.not(root.get("id").in(searchDto.getBlockedIds())));
            }

            // Фильтрация по автору (authorId как строка)
            if (searchDto.getAuthor() != null && !searchDto.getAuthor().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("authorId").as(String.class)), "%" + searchDto.getAuthor().toLowerCase() + "%"));
            }

            // Фильтрация по заголовку
            if (searchDto.getTitle() != null && !searchDto.getTitle().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("title")), "%" + searchDto.getTitle().toLowerCase() + "%"));
            }

            // Фильтрация по тексту поста
            if (searchDto.getPostText() != null && !searchDto.getPostText().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("postText")), "%" + searchDto.getPostText().toLowerCase() + "%"));
            }

            // Фильтрация по тегам
            if (searchDto.getTags() != null && !searchDto.getTags().isEmpty()) {
                predicates.add(root.get("tags").in(searchDto.getTags()));
            }

            // Фильтрация по дате публикации (диапазон)
            if (searchDto.getDateFrom() != null && searchDto.getDateTo() != null) {
                predicates.add(cb.between(root.get("publishDate"),
                        cb.literal(searchDto.getDateFrom()),
                        cb.literal(searchDto.getDateTo())));
            }

            // Фильтрация по статусу удаления
            if (searchDto.getIsDelete() != null) {
                predicates.add(cb.equal(root.get("isDelete"), searchDto.getIsDelete()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
