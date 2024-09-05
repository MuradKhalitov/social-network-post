package ru.skillbox.repository;
import ru.skillbox.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    //List<Comment> findByPostId(Long newsId);
    Page<Comment> findByPostId(Long postId, Pageable pageable);
//    @Query("SELECT c FROM Category c WHERE c.name = ?1")
//    Page<Category> findAllByCategory(String name, Pageable pageable);
}

