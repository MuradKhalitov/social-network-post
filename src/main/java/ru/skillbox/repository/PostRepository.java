package ru.skillbox.repository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.skillbox.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface PostRepository extends JpaRepository<Post, Long>, JpaSpecificationExecutor<Post> {
//    @Query("SELECT n FROM Post n WHERE n.postText = ?1")
//    Page<Post> findAllByNews(String postText, Pageable pageable);
//
//    List<Post> findByAuthorId(Long authorIds);
}

