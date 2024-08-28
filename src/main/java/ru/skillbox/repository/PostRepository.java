package ru.skillbox.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.skillbox.model.Post;

import java.util.List;
@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
//    @Query("SELECT n FROM News n WHERE n.content = ?1")
//    Page<News> findAllByNews(String content, Pageable pageable);
    List<Post> findByAuthorId(Long authorId);
}
