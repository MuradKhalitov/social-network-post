package ru.skillbox.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import ru.skillbox.AbstractTest;
import ru.skillbox.dto.comment.request.CommentDto;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

class CommentControllerTest extends AbstractTest {

    @Test
    @WithMockUser(username = AUTHOR_UUID)
    void createPostComment_shouldReturnCreatedStatus() throws Exception {
        Long id = 1L;

        CommentDto commentDto = new CommentDto();
        commentDto.setCommentText("Test comment");

        mockMvc.perform(post("/api/v1/post/{id}/comment", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(3L))
                .andExpect(jsonPath("$.authorId").value(AUTHOR_UUID))
                .andExpect(jsonPath("$.commentText").value("Test comment"));
    }

    @Test
    void createPostComment_shouldReturnUnauthorized() throws Exception {
        Long id = 1L;

        CommentDto commentDto = new CommentDto();
        commentDto.setCommentText("Test comment");

        mockMvc.perform(post("/api/v1/post/{id}/comment", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentDto)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(HttpServletResponse.SC_UNAUTHORIZED))
                .andExpect(jsonPath("$.error").value("Unauthorized"))
                .andExpect(jsonPath("$.message").exists());
    }
    @Test
    @WithMockUser(username = AUTHOR_UUID)
    void createPostComment_shouldReturnNotFound() throws Exception {
        Long id = 999L;

        CommentDto commentDto = new CommentDto();
        commentDto.setCommentText("Test comment");

        mockMvc.perform(post("/api/v1/post/{id}/comment", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = AUTHOR_UUID)
    void createSubComment_shouldReturnCreatedStatus() throws Exception {
        Long id = 1L;
        Long commentId = 1L;

        CommentDto subCommentDto = new CommentDto();
        subCommentDto.setCommentText("Test subcomment");

        mockMvc.perform(post("/api/v1/post/{id}/comment/{commentId}/subcomment", id, commentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(subCommentDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(3L))
                .andExpect(jsonPath("$.authorId").value(AUTHOR_UUID))
                .andExpect(jsonPath("$.commentText").value("Test subcomment"));
    }

    @Test
    void createSubComment_shouldReturnUnauthorized() throws Exception {
        Long id = 1L;
        Long commentId = 1L;

        CommentDto subCommentDto = new CommentDto();
        subCommentDto.setCommentText("Test subcomment");

        mockMvc.perform(post("/api/v1/post/{id}/comment/{commentId}/subcomment", id, commentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(subCommentDto)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(HttpServletResponse.SC_UNAUTHORIZED))
                .andExpect(jsonPath("$.error").value("Unauthorized"))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @WithMockUser(username = AUTHOR_UUID)
    void createSubComment_shouldReturnNotFound() throws Exception {
        Long id = 1L;
        Long commentId = 999L;

        CommentDto subCommentDto = new CommentDto();
        subCommentDto.setCommentText("Test subcomment");

        mockMvc.perform(post("/api/v1/post/{id}/comment/{commentId}/subcomment", id, commentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(subCommentDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = AUTHOR_UUID)
    void getCommentsByPostId_shouldReturnOkStatus() throws Exception {
        Long id = 1L;

        mockMvc.perform(get("/api/v1/post/{id}/comment", id)
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "id,asc"))
                .andExpect(status().isOk());
    }

    @Test
    void getCommentsByPostId_shouldReturnUnauthorized() throws Exception {
        Long id = 1L;

        mockMvc.perform(get("/api/v1/post/{id}/comment", id)
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "id,asc"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(HttpServletResponse.SC_UNAUTHORIZED))
                .andExpect(jsonPath("$.error").value("Unauthorized"))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @WithMockUser(username = AUTHOR_UUID)
    void getCommentsByPostId_shouldReturnNotFound() throws Exception {
        Long id = 999L;

        mockMvc.perform(get("/api/v1/post/{id}/comment", id)
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "id,asc"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = AUTHOR_UUID)
    void getSubCommentById_shouldReturnOkStatus() throws Exception {
        Long id = 1L;
        Long commentId = 1L;

        mockMvc.perform(get("/api/v1/post/{id}/comment/{commentId}/subcomment", id, commentId)
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "id,asc"))
                .andExpect(status().isOk());
    }

    @Test
    void getSubCommentById_shouldReturnUnauthorized() throws Exception {
        Long id = 1L;
        Long commentId = 1L;

        mockMvc.perform(get("/api/v1/post/{id}/comment/{commentId}/subcomment", id, commentId)
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "id,asc"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(HttpServletResponse.SC_UNAUTHORIZED))
                .andExpect(jsonPath("$.error").value("Unauthorized"))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @WithMockUser(username = AUTHOR_UUID)
    void updateComment_shouldReturnCreatedStatus() throws Exception {
        Long id = 1L;
        Long commentId = 1L;

        CommentDto commentDto = new CommentDto();
        commentDto.setCommentText("Edited comment");

        mockMvc.perform(put("/api/v1/post/{id}/comment/{commentId}", id, commentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentDto)))
                .andExpect(status().isCreated());
    }
    @Test
    void updateComment_shouldReturnUnauthorized() throws Exception {
        Long id = 1L;
        Long commentId = 1L;

        CommentDto commentDto = new CommentDto();
        commentDto.setCommentText("Edited comment");

        mockMvc.perform(put("/api/v1/post/{id}/comment/{commentId}", id, commentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentDto)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(HttpServletResponse.SC_UNAUTHORIZED))
                .andExpect(jsonPath("$.error").value("Unauthorized"))
                .andExpect(jsonPath("$.message").exists());
    }
    @Test
    @WithMockUser(username = AUTHOR_UUID)
    void updateComment_shouldReturnNotFound() throws Exception {
        Long id = 1L;
        Long commentId = 999L;

        CommentDto commentDto = new CommentDto();
        commentDto.setCommentText("Edited comment");

        mockMvc.perform(put("/api/v1/post/{id}/comment/{commentId}", id, commentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = AUTHOR_UUID)
    void deleteComment_shouldReturnOkStatus() throws Exception {
        Long id = 1L;
        Long commentId = 1L;

        mockMvc.perform(delete("/api/v1/post/{id}/comment/{commentId}", id, commentId))
                .andExpect(status().isOk());
    }

    @Test
    void deleteComment_shouldReturnUnauthorized() throws Exception {
        Long id = 1L;
        Long commentId = 1L;

        mockMvc.perform(delete("/api/v1/post/{id}/comment/{commentId}", id, commentId))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(HttpServletResponse.SC_UNAUTHORIZED))
                .andExpect(jsonPath("$.error").value("Unauthorized"))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @WithMockUser(username = AUTHOR_UUID)
    void deleteComment_shouldReturnNotFound() throws Exception {
        Long id = 1L;
        Long commentId = 999L;

        mockMvc.perform(delete("/api/v1/post/{id}/comment/{commentId}", id, commentId))
                .andExpect(status().isNotFound());
    }
}
