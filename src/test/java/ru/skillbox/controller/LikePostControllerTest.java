package ru.skillbox.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import ru.skillbox.dto.like_post.AddReactionDto;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
class LikePostControllerTest extends AbstractTest {

    @Test
    @WithMockUser(username = AUTHOR_UUID)
    void createLike_shouldReturnCreatedStatus() throws Exception {
        Long id = 1L;

        AddReactionDto addReactionDto = new AddReactionDto();
        addReactionDto.setReactionType("funny");

        mockMvc.perform(post("/api/v1/post/{id}/like", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addReactionDto)))
                .andExpect(status().isCreated());
    }

    @Test
    void createLike_shouldReturnUnauthorized() throws Exception {
        Long id = 1L;

        AddReactionDto addReactionDto = new AddReactionDto();
        addReactionDto.setReactionType("funny");

        mockMvc.perform(post("/api/v1/post/{id}/like", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addReactionDto)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(HttpServletResponse.SC_UNAUTHORIZED))
                .andExpect(jsonPath("$.error").value("Unauthorized"))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @WithMockUser(username = AUTHOR_UUID)
    void createLike_shouldReturnNotFoundStatus_whenPostDoesNotExist() throws Exception {
        Long id = 999L;

        AddReactionDto addReactionDto = new AddReactionDto();
        addReactionDto.setReactionType("funny");

        mockMvc.perform(post("/api/v1/post/{id}/like", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addReactionDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = AUTHOR_UUID)
    void deleteLike_shouldReturnCreatedStatus() throws Exception {
        Long id = 1L;

        mockMvc.perform(delete("/api/v1/post/{id}/like", id))
                .andExpect(status().isCreated());
    }

    @Test
    void deleteLike_shouldReturnUnauthorized() throws Exception {
        Long id = 1L;

        mockMvc.perform(delete("/api/v1/post/{id}/like", id))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(HttpServletResponse.SC_UNAUTHORIZED))
                .andExpect(jsonPath("$.error").value("Unauthorized"))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @WithMockUser(username = AUTHOR_UUID)
    void deleteLike_shouldReturnNotFoundStatus_whenPostDoesNotExist() throws Exception {
        Long id = 999L;

        mockMvc.perform(delete("/api/v1/post/{id}/like", id))
                .andExpect(status().isNotFound());
    }
}
