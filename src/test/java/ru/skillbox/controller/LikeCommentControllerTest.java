package ru.skillbox.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

class LikeCommentControllerTest extends AbstractTest {


    @Test
    @WithMockUser(username = AUTHOR_UUID)
    void createLike_shouldReturnCreatedStatus() throws Exception {
        Long id = 1L;
        Long commentId = 1L;

        mockMvc.perform(post("/api/v1/post/{id}/comment/{commentId}/like", id, commentId))
                .andExpect(status().isCreated());
    }

    @Test
    void createLike_shouldReturnUnauthorized() throws Exception {
        Long id = 1L;
        Long commentId = 1L;

        mockMvc.perform(post("/api/v1/post/{id}/comment/{commentId}/like", id, commentId))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(HttpServletResponse.SC_UNAUTHORIZED))
                .andExpect(jsonPath("$.error").value("Unauthorized"))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @WithMockUser(username = AUTHOR_UUID)
    void createLike_shouldReturnNotFoundStatus_whenCommentDoesNotExist() throws Exception {
        Long id = 1L;
        Long commentId = 999L;

        mockMvc.perform(post("/api/v1/post/{id}/comment/{commentId}/like", id, commentId))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = AUTHOR_UUID)
    void deleteLike_shouldReturnCreatedStatus() throws Exception {
        Long id = 1L;
        Long commentId = 1L;

        mockMvc.perform(delete("/api/v1/post/{id}/comment/{commentId}/like", id, commentId))
                .andExpect(status().isCreated());
    }

    @Test
    void deleteLike_shouldReturnUnauthorized() throws Exception {
        Long id = 1L;
        Long commentId = 1L;

        mockMvc.perform(delete("/api/v1/post/{id}/comment/{commentId}/like", id, commentId))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(HttpServletResponse.SC_UNAUTHORIZED))
                .andExpect(jsonPath("$.error").value("Unauthorized"))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @WithMockUser(username = AUTHOR_UUID)
    void deleteLike_shouldReturnNotFoundStatus_whenCommentDoesNotExist() throws Exception {
        Long id = 1L;
        Long commentId = 999L;

        mockMvc.perform(delete("/api/v1/post/{id}/comment/{commentId}/like", id, commentId))
                .andExpect(status().isNotFound());
    }
}
