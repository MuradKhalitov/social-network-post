package ru.skillbox.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skillbox.model.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostId(Long postId);
}
