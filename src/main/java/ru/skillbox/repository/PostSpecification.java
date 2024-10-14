package ru.skillbox.repository;

import org.springframework.data.jpa.domain.Specification;
import ru.skillbox.dto.post.request.PostSearchDto;
import ru.skillbox.model.Post;

import jakarta.persistence.criteria.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PostSpecification {

    // Приватный конструктор для предотвращения создания экземпляров класса
    private PostSpecification() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static Specification<Post> filterBySearchDto(PostSearchDto postSearchDto) {
        return (Root<Post> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            addIdPredicates(postSearchDto, root, predicates);
            addAccountIdPredicates(postSearchDto, root, predicates);
            addBlockedIdPredicates(postSearchDto, root, cb, predicates);
            addAuthorPredicate(postSearchDto, root, cb, predicates);
            addTitlePredicate(postSearchDto, root, cb, predicates);
            addPostTextPredicate(postSearchDto, root, cb, predicates);
            addTagPredicates(postSearchDto, root, predicates);
            addDateRangePredicate(postSearchDto, root, cb, predicates);
            addIsDeletedPredicate(postSearchDto, root, cb, predicates);
            addWithFriendsPredicate(postSearchDto, root, cb, predicates);

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static void addIdPredicates(PostSearchDto postSearchDto, Root<Post> root, List<Predicate> predicates) {
        Optional.ofNullable(postSearchDto.getIds())
                .filter(ids -> !ids.isEmpty())
                .ifPresent(ids -> predicates.add(root.get("id").in(ids)));
    }

    private static void addAccountIdPredicates(PostSearchDto postSearchDto, Root<Post> root, List<Predicate> predicates) {
        Optional.ofNullable(postSearchDto.getAccountIds())
                .filter(accountIds -> !accountIds.isEmpty())
                .ifPresent(accountIds -> predicates.add(root.get("authorId").in(accountIds)));
    }

    private static void addBlockedIdPredicates(PostSearchDto postSearchDto, Root<Post> root, CriteriaBuilder cb, List<Predicate> predicates) {
        Optional.ofNullable(postSearchDto.getBlockedIds())
                .filter(blockedIds -> !blockedIds.isEmpty())
                .ifPresent(blockedIds -> predicates.add(cb.not(root.get("id").in(blockedIds))));
    }

    private static void addAuthorPredicate(PostSearchDto postSearchDto, Root<Post> root, CriteriaBuilder cb, List<Predicate> predicates) {
        Optional.ofNullable(postSearchDto.getAuthor())
                .filter(author -> !author.isEmpty())
                .ifPresent(author -> predicates.add(cb.like(cb.lower(root.get("author").as(String.class)), "%" + author.toLowerCase() + "%")));
    }

    private static void addTitlePredicate(PostSearchDto postSearchDto, Root<Post> root, CriteriaBuilder cb, List<Predicate> predicates) {
        Optional.ofNullable(postSearchDto.getTitle())
                .filter(title -> !title.isEmpty())
                .ifPresent(title -> predicates.add(cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%")));
    }

    private static void addPostTextPredicate(PostSearchDto postSearchDto, Root<Post> root, CriteriaBuilder cb, List<Predicate> predicates) {
        Optional.ofNullable(postSearchDto.getPostText())
                .filter(postText -> !postText.isEmpty())
                .ifPresent(postText -> predicates.add(cb.like(cb.lower(root.get("postText")), "%" + postText.toLowerCase() + "%")));
    }

    private static void addTagPredicates(PostSearchDto postSearchDto, Root<Post> root, List<Predicate> predicates) {
        Optional.ofNullable(postSearchDto.getTags())
                .filter(tags -> !tags.isEmpty())
                .ifPresent(tags -> predicates.add(root.get("tags").in(tags)));
    }

    private static void addDateRangePredicate(PostSearchDto postSearchDto, Root<Post> root, CriteriaBuilder cb, List<Predicate> predicates) {
        if (postSearchDto.getDateFrom() != null && postSearchDto.getDateTo() != null) {
            predicates.add(cb.between(root.get("publishDate"),
                    cb.literal(postSearchDto.getDateFrom()),
                    cb.literal(postSearchDto.getDateTo())));
        }
    }

    private static void addIsDeletedPredicate(PostSearchDto postSearchDto, Root<Post> root, CriteriaBuilder cb, List<Predicate> predicates) {
        Optional.ofNullable(postSearchDto.getIsDeleted())
                .ifPresent(isDeleted -> predicates.add(cb.equal(root.get("isDeleted"), isDeleted)));
    }

    private static void addWithFriendsPredicate(PostSearchDto postSearchDto, Root<Post> root, CriteriaBuilder cb, List<Predicate> predicates) {
        if (Boolean.TRUE.equals(postSearchDto.getWithFriends())) {
            // Здесь предполагается, что есть связь с сущностью друзей
            // Например, можно использовать Join, если в модели Post есть связь с друзьями
            predicates.add(cb.isTrue(root.join("authorId").get("isFriend")));
        }
    }
}


