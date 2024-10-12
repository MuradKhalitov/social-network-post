package ru.skillbox.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.skillbox.AbstractTest;
import ru.skillbox.dto.comment.request.CommentDto;
import ru.skillbox.dto.comment.response.PageCommentDto;
import ru.skillbox.service.CommentService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CommentControllerTest extends AbstractTest {

    @Mock
    private CommentService commentService;

    @InjectMocks
    private CommentController commentController;

    private static final String BASE_URL = "/api/v1/post/";

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(commentController).build();
    }

    @Test
    @WithMockUser(username = AUTHOR_UUID)
    void createPostComment_shouldReturnCreatedStatus() throws Exception {
        Long postId = 1L;

        CommentDto commentDto = new CommentDto();
        commentDto.setCommentText("Тестовый комментарий");

//        when(commentService.createPostComment(any(Long.class), any(CommentDto.class)))
//                .thenReturn(commentDto);

        mockMvc.perform(post(BASE_URL + postId + "/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentDto)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username = AUTHOR_UUID)
    void createSubComment_shouldReturnCreatedStatus() throws Exception {
        Long postId = 1L;
        Long commentId = 1L;

        CommentDto subCommentDto = new CommentDto();
        subCommentDto.setCommentText("Тестовый подкомментарий");

        when(commentService.createSubComment(any(Long.class), any(CommentDto.class), any(Long.class)))
                .thenReturn(subCommentDto);

        mockMvc.perform(post(BASE_URL + postId + "/comment/" + commentId + "/subcomment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(subCommentDto)))
                .andExpect(status().isCreated());
    }


    @Test
    @WithMockUser(username = AUTHOR_UUID)
    void getCommentsByPostId_shouldReturnOkStatus() throws Exception {
        Long postId = 1L;

        PageCommentDto pageCommentDto = new PageCommentDto();
        when(commentService.getPostComments(any(Long.class), any()))
                .thenReturn(pageCommentDto);

        mockMvc.perform(get(BASE_URL + postId + "/comment")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "id,asc"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = AUTHOR_UUID)
    void getSubCommentById_shouldReturnOkStatus() throws Exception {
        Long postId = 1L;
        Long commentId = 1L;

        PageCommentDto pageCommentDto = new PageCommentDto();

        // Используем lenient, если вызов заглушки не критичен для теста
        when(commentService.getSubComments(any(Long.class), any()))
                .thenReturn(pageCommentDto);

        mockMvc.perform(get(BASE_URL + postId + "/comment/" + commentId + "/subcomment")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "id,asc"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = AUTHOR_UUID)
    void updateComment_shouldReturnCreatedStatus() throws Exception {
        Long postId = 1L;
        Long commentId = 1L;

        CommentDto commentDto = new CommentDto();
        commentDto.setCommentText("Обновленный комментарий");

        when(commentService.updateComment(any(Long.class), any(Long.class), any(CommentDto.class)))
                .thenReturn(commentDto);

        mockMvc.perform(put(BASE_URL + postId + "/comment/" + commentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentDto)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username = AUTHOR_UUID)
    void deleteComment_shouldReturnOkStatus() throws Exception {
        Long postId = 1L;
        Long commentId = 1L;

        mockMvc.perform(delete(BASE_URL + postId + "/comment/" + commentId))
                .andExpect(status().isOk());
    }
}
