package ru.skillbox;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import ru.skillbox.model.Comment;
import ru.skillbox.model.LikeComment;
import ru.skillbox.model.LikePost;
import ru.skillbox.model.Post;
import ru.skillbox.repository.CommentRepository;
import ru.skillbox.repository.LikeCommentRepository;
import ru.skillbox.repository.LikePostRepository;
import ru.skillbox.repository.PostRepository;

import java.time.LocalDateTime;
import java.util.UUID;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Testcontainers
public class AbstractTest {

    protected static PostgreSQLContainer<?> postgreSQLContainer;

    static {
        DockerImageName postgres = DockerImageName.parse("postgres:12.3");
        postgreSQLContainer = new PostgreSQLContainer<>(postgres).withReuse(true);
        postgreSQLContainer.start();
    }

    @DynamicPropertySource
    public static void registerProperties(DynamicPropertyRegistry registry) {
        String jdbcUrl = postgreSQLContainer.getJdbcUrl();
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.datasource.url", () -> jdbcUrl);
    }

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected PostRepository postRepository;
    @Autowired
    protected CommentRepository commentRepository;
    @Autowired
    protected LikePostRepository likePostRepository;
    @Autowired
    protected LikeCommentRepository likeCommentRepository;
    protected ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    protected JdbcTemplate jdbcTemplate;
    protected static final String BASE_URL = "/api/v1/post/";
    protected static final String AUTHOR_UUID = "10000000-0000-0000-0000-000000000200";


    @BeforeEach
    public void setup() {
        jdbcTemplate.execute("ALTER SEQUENCE post_schema.post_id_seq RESTART WITH 1");
        jdbcTemplate.execute("ALTER SEQUENCE post_schema.comment_id_seq RESTART WITH 1");

        // Создаем пост с id 1
        Post post = new Post();
        post.setTitle("Test Post");
        post.setPostText("This is a test post");
        post.setAuthorId(UUID.fromString(AUTHOR_UUID));
        post.setPublishDate(LocalDateTime.now());
        post.setTime(LocalDateTime.now());
        post.setTimeChanged(LocalDateTime.now());
        post.setBlocked(false);
        post.setDeleted(false);
        postRepository.save(post);

        // Создаем комментарий с id 1
        Comment comment = new Comment();
        comment.setAuthorId(UUID.fromString(AUTHOR_UUID));
        comment.setCommentText("Тестовый комментарий");
        comment.setPost(post);
        commentRepository.save(comment);

        // Создаем лайк поста
        LikePost likePost = new LikePost();
        likePost.setAuthorId(UUID.fromString(AUTHOR_UUID));
        likePost.setReactionType("LIKE");
        likePost.setCreatedAt(LocalDateTime.now());
        likePost.setPost(post);
        likePostRepository.save(likePost);

        // Создаем лайк комментария
        LikeComment likeComment = new LikeComment();
        likeComment.setAuthorId(UUID.fromString(AUTHOR_UUID));
        likeComment.setComment(comment);
        likeCommentRepository.save(likeComment);

    }

    @AfterEach
    public void afterEach() {
        likeCommentRepository.deleteAll();
        likePostRepository.deleteAll();
        commentRepository.deleteAll();
        postRepository.deleteAll();

    }
}
