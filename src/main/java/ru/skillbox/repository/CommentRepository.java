package ru.skillbox.repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.skillbox.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    //Optional<Comment> findByIdAndAuthorId(Long commentId, UUID authorId);
    //List<Comment> findByPostId(Long newsId);
    Page<Comment> findByPostId(Long postId, Pageable pageable);
    Optional<Comment> findById(Long id);
//    @Query("SELECT c FROM Category c WHERE c.name = ?1")
//    Page<Category> findAllByCategory(String name, Pageable pageable);

    @Query("SELECT COUNT(c) FROM Comment c WHERE c.parent.id = :parentId")
    int countSubCommentsByParentId(@Param("parentId") Long parentId);
}

