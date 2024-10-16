package ru.skillbox.repository;

import org.springframework.data.jpa.domain.Specification;
import ru.skillbox.dto.post.request.PostSearchDto;
import ru.skillbox.model.Post;

import jakarta.persistence.criteria.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PostSpecification {
    private PostSpecification() {
    }

    public static Specification<Post> filterBySearchDto(PostSearchDto postSearchDto, List<UUID> friends) {
        return (Root<Post> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            addFilterByIds(predicates, root, postSearchDto);
            addFilterByAccountIds(predicates, root, postSearchDto);
            addFilterByBlockedIds(predicates, root, cb, postSearchDto);
            addFilterByTitle(predicates, root, cb, postSearchDto);
            addFilterByPostText(predicates, root, cb, postSearchDto);
            addFilterByTags(predicates, root, postSearchDto);
            addFilterByPublishDate(predicates, root, cb, postSearchDto);
            addFilterByIsDeleted(predicates, root, cb, postSearchDto);
            addFilterByFriends(predicates, root, friends, postSearchDto);

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static void addFilterByIds(List<Predicate> predicates, Root<Post> root, PostSearchDto postSearchDto) {
        if (postSearchDto.getIds() != null && !postSearchDto.getIds().isEmpty()) {
            predicates.add(root.get("id").in(postSearchDto.getIds()));
        }
    }

    private static void addFilterByAccountIds(List<Predicate> predicates, Root<Post> root, PostSearchDto postSearchDto) {
        if (postSearchDto.getAccountIds() != null && !postSearchDto.getAccountIds().isEmpty()) {
            predicates.add(root.get("authorId").in(postSearchDto.getAccountIds()));
        }
    }

    private static void addFilterByBlockedIds(List<Predicate> predicates, Root<Post> root, CriteriaBuilder cb, PostSearchDto postSearchDto) {
        if (postSearchDto.getBlockedIds() != null && !postSearchDto.getBlockedIds().isEmpty()) {
            predicates.add(cb.not(root.get("id").in(postSearchDto.getBlockedIds())));
        }
    }

    private static void addFilterByTitle(List<Predicate> predicates, Root<Post> root, CriteriaBuilder cb, PostSearchDto postSearchDto) {
        if (postSearchDto.getTitle() != null && !postSearchDto.getTitle().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("title")), "%" + postSearchDto.getTitle().toLowerCase() + "%"));
        }
    }

    private static void addFilterByPostText(List<Predicate> predicates, Root<Post> root, CriteriaBuilder cb, PostSearchDto postSearchDto) {
        if (postSearchDto.getPostText() != null && !postSearchDto.getPostText().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("postText")), "%" + postSearchDto.getPostText().toLowerCase() + "%"));
        }
    }

    private static void addFilterByTags(List<Predicate> predicates, Root<Post> root, PostSearchDto postSearchDto) {
        if (postSearchDto.getTags() != null && !postSearchDto.getTags().isEmpty()) {
            predicates.add(root.get("tags").in(postSearchDto.getTags()));
        }
    }

    private static void addFilterByPublishDate(List<Predicate> predicates, Root<Post> root, CriteriaBuilder cb, PostSearchDto postSearchDto) {
        if (postSearchDto.getDateFrom() != null && postSearchDto.getDateTo() != null) {
            predicates.add(cb.between(root.get("publishDate"), postSearchDto.getDateFrom(), postSearchDto.getDateTo()));
        }
    }

    private static void addFilterByIsDeleted(List<Predicate> predicates, Root<Post> root, CriteriaBuilder cb, PostSearchDto postSearchDto) {
        if (postSearchDto.getIsDeleted() != null) {
            predicates.add(cb.equal(root.get("isDeleted"), postSearchDto.getIsDeleted()));
        }
    }

    private static void addFilterByFriends(List<Predicate> predicates, Root<Post> root, List<UUID> friends, PostSearchDto postSearchDto) {
        if (postSearchDto.getWithFriends() != null && postSearchDto.getWithFriends() && !friends.isEmpty()) {
            predicates.add(root.get("authorId").in(friends));
        }
    }
}


