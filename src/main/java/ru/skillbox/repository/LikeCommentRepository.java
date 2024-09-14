package ru.skillbox.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skillbox.model.LikeComment;
import ru.skillbox.model.LikePost;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LikeCommentRepository extends JpaRepository<LikeComment, Long> {
    Optional<LikeComment> findByCommentIdAndAuthorId(Long commentId, UUID authorId);
    List<LikeComment> findByCommentId(Long commentId);

}

