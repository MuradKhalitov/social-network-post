package ru.skillbox.controller;//package ru.skillbox.controller;
//
//import jakarta.servlet.http.HttpServletResponse;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.http.MediaType;
//import org.springframework.security.test.context.support.WithMockUser;
//import ru.skillbox.AbstractTest;
//import ru.skillbox.client.AccountFeignClient;
//import ru.skillbox.dto.post.request.PostDto;
//import ru.skillbox.dto.post.response.PagePostDto;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//import static reactor.core.publisher.Mono.when;
//
//class PostControllerTest extends AbstractTest {
//
//    @Test
//    @DisplayName("GetPostById, should return 200")
//    @WithMockUser(username = AUTHOR_UUID)
//    void testGetPostById_shouldReturnOk() throws Exception {
//        Long id = 1L;
//
//        mockMvc.perform(get(BASE_URL + "/{id}", id))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.id").value(id.intValue()))
//                .andExpect(jsonPath("$.title").value("Test Post"))
//                .andExpect(jsonPath("$.authorId").value(AUTHOR_UUID));
//    }
//
//    @Test
//    @DisplayName("GetPostById, should return 401")
//    void testGetPostById_shouldReturnUnauthorized() throws Exception {
//        Long id = 1L;
//
//        mockMvc.perform(get(BASE_URL + "/{id}", id))
//                .andExpect(status().isUnauthorized())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.status").value(HttpServletResponse.SC_UNAUTHORIZED))
//                .andExpect(jsonPath("$.error").value("Unauthorized"))
//                .andExpect(jsonPath("$.message").exists());
//    }
//    @Test
//    @DisplayName("GetPostById, should return 404")
//    @WithMockUser(username = AUTHOR_UUID)
//    void testGetAccountById_shouldReturnNotFound() throws Exception {
//        Long id = 999L;
//
//        mockMvc.perform(get(BASE_URL + "/{id}", id))
//                .andExpect(status().isNotFound());
//    }
//
//    @Test
//    @WithMockUser(username = AUTHOR_UUID)
//    void createPost_shouldReturnCreatedStatus() throws Exception {
//        PostDto postDto = new PostDto();
//        postDto.setTitle("Test Post");
//
//        mockMvc.perform(post(BASE_URL)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(postDto)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.id").value(2L))
//                .andExpect(jsonPath("$.title").value("Test Post"));
//    }
//
//    @Test
//    @WithMockUser(username = AUTHOR_UUID)
//    void updatePost_shouldReturnCreatedStatus() throws Exception {
//        PostDto postDto = new PostDto();
//        postDto.setTitle("Updated Post");
//
//        mockMvc.perform(put(BASE_URL)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(postDto)))
//                .andExpect(status().isCreated()); // Проверяем только статус
//    }
//
//
//
//
//    @Test
//    @WithMockUser(username = AUTHOR_UUID)
//    void deletePost_shouldReturnOkStatus() throws Exception {
//        Long id = 1L;
//
//        mockMvc.perform(delete(BASE_URL + "/{id}", id)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    @WithMockUser(username = AUTHOR_UUID)
//    void searchPosts_shouldReturnOkStatus() throws Exception {
//        PagePostDto pagePostDto = new PagePostDto();
//        // Здесь можно настроить pagePostDto
//
//        mockMvc.perform(get(BASE_URL)
//                        .param("page", "0")
//                        .param("size", "10")
//                        .param("sort", "id,asc")
//                        .param("isDeleted", "false"))
//                .andExpect(status().isOk());
//    }
//}

import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import ru.skillbox.AbstractTest;
import ru.skillbox.client.AccountFeignClient;
import ru.skillbox.dto.AccountDto;
import ru.skillbox.dto.post.request.PostDto;

import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PostControllerTest extends AbstractTest {

    @MockBean
    private AccountFeignClient accountFeignClient;

    @Test
    @DisplayName("GetPostById, should return 200")
    @WithMockUser(username = AUTHOR_UUID)
    void testGetPostById_shouldReturnOk() throws Exception {
        Long id = 1L;

        mockMvc.perform(get(BASE_URL + "/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(id.intValue()))
                .andExpect(jsonPath("$.title").value("Test Post"))
                .andExpect(jsonPath("$.authorId").value(AUTHOR_UUID));
    }

    @Test
    @DisplayName("GetPostById, should return 401")
    void testGetPostById_shouldReturnUnauthorized() throws Exception {
        Long id = 1L;

        mockMvc.perform(get(BASE_URL + "/{id}", id))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(HttpServletResponse.SC_UNAUTHORIZED))
                .andExpect(jsonPath("$.error").value("Unauthorized"))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("GetPostById, should return 404")
    @WithMockUser(username = AUTHOR_UUID)
    void testGetPostById_shouldReturnNotFound() throws Exception {
        Long id = 999L;

        mockMvc.perform(get(BASE_URL + "/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = AUTHOR_UUID)
    void createPost_shouldReturnCreatedStatus() throws Exception {
        PostDto postDto = new PostDto();
        postDto.setTitle("Test Post");
        postDto.setPostText("This is a test post."); // Добавьте текст поста, если он требуется

        when(accountFeignClient.getAccountById(UUID.fromString(AUTHOR_UUID)))
                .thenReturn(new AccountDto("John", "Doe"));

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2L))
                .andExpect(jsonPath("$.title").value("Test Post"));
    }

    @Test
    @WithMockUser(username = AUTHOR_UUID)
    void updatePost_shouldReturnCreatedStatus() throws Exception {
        Long id = 1L; // Указываем ID поста для обновления
        PostDto postDto = new PostDto();
        postDto.setId(id); // Указываем ID поста
        postDto.setTitle("Updated Post");

        mockMvc.perform(put(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postDto)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username = AUTHOR_UUID)
    void deletePost_shouldReturnOkStatus() throws Exception {
        Long id = 1L;

        mockMvc.perform(delete(BASE_URL + "/{id}", id))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = AUTHOR_UUID)
    void searchPosts_shouldReturnOkStatus() throws Exception {
        mockMvc.perform(get(BASE_URL)
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "id,asc")
                        .param("isDeleted", "false"))
                .andExpect(status().isOk());
    }
}

