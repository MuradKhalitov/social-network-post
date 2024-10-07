package ru.skillbox.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.skillbox.AbstractTest;
import ru.skillbox.dto.post.request.PostDto;
import ru.skillbox.dto.post.response.PagePostDto;
import ru.skillbox.service.PostService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
class PostControllerTest extends AbstractTest {
    @Mock
    private PostService postService;
    @InjectMocks
    private PostController postController;
    @BeforeEach
    public void setup() {
        super.setup();
        mockMvc = MockMvcBuilders.standaloneSetup(postController).build();
    }

    @Test
    @WithMockUser(username = AUTHOR_UUID)
    void getPostById_shouldReturnPostDto() throws Exception {
        Long postId = 1L;

        PostDto postDto = new PostDto();
        postDto.setId(postId);
        postDto.setTitle("Test Post");

        // Мокаем сервис и задаем поведение
        when(postService.getPostById(any(Long.class))).thenReturn(postDto);

        mockMvc.perform(get(BASE_URL + postId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(postId))
                .andExpect(jsonPath("$.title").value("Test Post"));
    }

    @Test
    @WithMockUser(username = AUTHOR_UUID)
    void createPost_shouldReturnCreatedStatus() throws Exception {
        // Создание DTO для запроса
        PostDto postDto = new PostDto();
        postDto.setTitle("Test Post");

        // Mock для postService.createPost
        when(postService.createPost(any(PostDto.class))).thenAnswer(invocation -> {
            PostDto result = invocation.getArgument(0);
            result.setId(1L); // Устанавливаем id для сохраненного поста
            return result;
        });

        // Выполняем POST-запрос
        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postDto)))
                .andExpect(status().isCreated()) // Ожидаем статус 201 Created
                .andExpect(jsonPath("$.id").value(1L)) // Проверяем, что ID возвращенного поста = 1
                .andExpect(jsonPath("$.title").value("Test Post")); // Проверяем название поста
    }

    @Test
    @WithMockUser(username = AUTHOR_UUID)
    void updatePost_shouldReturnCreatedStatus() throws Exception {
        PostDto postDto = new PostDto();
        postDto.setTitle("Updated Post");

        // Если метод возвращает PostDto, используйте when:
        when(postService.updatePost(any(PostDto.class))).thenReturn(postDto);

        mockMvc.perform(put(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postDto)))
                .andExpect(status().isCreated()); // Проверяем только статус
    }




    @Test
    @WithMockUser(username = AUTHOR_UUID)
    void deletePost_shouldReturnOkStatus() throws Exception {
        Long postId = 1L;

        mockMvc.perform(delete(BASE_URL + postId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = AUTHOR_UUID)
    void searchPosts_shouldReturnOkStatus() throws Exception {
        PagePostDto pagePostDto = new PagePostDto();
        // Здесь можно настроить pagePostDto

       when(postService.searchPosts(any(), any())).thenReturn(pagePostDto);

        mockMvc.perform(get(BASE_URL)
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "id,asc")
                        .param("isDeleted", "false"))
                .andExpect(status().isOk());
    }
}
