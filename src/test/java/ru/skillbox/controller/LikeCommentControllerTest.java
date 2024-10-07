package ru.skillbox.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import ru.skillbox.AbstractTest;
import ru.skillbox.dto.likeComment.LikeCommentDto;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class LikeCommentControllerTest extends AbstractTest {

    private static final String BASE_URL = "/api/v1/post/";

    @Test
    @WithMockUser(username = AUTHOR_UUID)
    void createLike_shouldReturnCreatedStatus() throws Exception {
        Long postId = 1L;
        Long commentId = 1L;
        LikeCommentDto likeCommentDto = new LikeCommentDto();
        String requestBody = objectMapper.writeValueAsString(likeCommentDto);

        mockMvc.perform(post(BASE_URL + postId + "/comment/" + commentId + "/like")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username = AUTHOR_UUID)
    void deleteLike_shouldReturnCreatedStatus() throws Exception {
        Long postId = 1L;
        Long commentId = 1L;

        mockMvc.perform(delete(BASE_URL + postId + "/comment/" + commentId + "/like"))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username = AUTHOR_UUID)
    void createLike_shouldReturnNotFoundStatus_whenCommentDoesNotExist() throws Exception {
        Long postId = 1L;
        Long commentId = 999L; // Комментарий не существует

        mockMvc.perform(post(BASE_URL + postId + "/comment/" + commentId + "/like")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = AUTHOR_UUID)
    void deleteLike_shouldReturnNotFoundStatus_whenCommentDoesNotExist() throws Exception {
        Long postId = 1L;
        Long commentId = 999L; // Комментарий не существует

        mockMvc.perform(delete(BASE_URL + postId + "/comment/" + commentId + "/like"))
                .andExpect(status().isNotFound());
    }
}
