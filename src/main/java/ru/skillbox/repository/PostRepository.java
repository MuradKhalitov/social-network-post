package ru.skillbox.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skillbox.model.Post;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByAuthorId(Long authorId);
}
