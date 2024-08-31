package ru.skillbox.repository;
import ru.skillbox.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsRepository extends JpaRepository<Post, Long> {
    @Query("SELECT n FROM Post n WHERE n.postText = ?1")
    Page<Post> findAllByNews(String postText, Pageable pageable);

    List<Post> findByAuthorId(Long authorIds);
}

