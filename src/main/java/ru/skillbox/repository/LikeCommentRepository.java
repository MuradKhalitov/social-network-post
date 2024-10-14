package ru.skillbox.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skillbox.model.LikeComment;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface LikeCommentRepository extends JpaRepository<LikeComment, Long> {
    Optional<LikeComment> findByCommentIdAndAuthorId(Long commentId, UUID authorId);
}

