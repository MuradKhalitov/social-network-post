package ru.skillbox.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skillbox.model.LikePost;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LikePostRepository extends JpaRepository<LikePost, Long> {
    Optional<LikePost> findByPostIdAndAuthorId(Long postId, UUID authorId);
    List<LikePost> findByPostId(Long postId);
}

