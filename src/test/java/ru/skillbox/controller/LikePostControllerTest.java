package ru.skillbox.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import ru.skillbox.AbstractTest;
import ru.skillbox.dto.likePost.AddReactionDto;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
class LikePostControllerTest extends AbstractTest {

    @Test
    @WithMockUser(username = AUTHOR_UUID)
    void createLike_shouldReturnCreatedStatus() throws Exception {
        Long postId = 1L;

        // Создаем DTO для передачи в тело запроса
        AddReactionDto addReactionDto = new AddReactionDto();
        addReactionDto.setReactionType("LIKE");  // Пример типа реакции

        // Преобразуем DTO в JSON
        String requestBody = objectMapper.writeValueAsString(addReactionDto);

        mockMvc.perform(post(BASE_URL + postId + "/like")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated());  // Проверяем, что статус ответа CREATED (201)
    }

    @Test
    @WithMockUser(username = AUTHOR_UUID)
    void deleteLike_shouldReturnCreatedStatus() throws Exception {
        Long postId = 1L;

        mockMvc.perform(delete(BASE_URL + postId + "/like"))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username = AUTHOR_UUID)
    void createLike_shouldReturnNotFoundStatus_whenPostDoesNotExist() throws Exception {
        Long postId = 999L;  // Несуществующий пост

        // Создаем DTO для передачи в тело запроса
        AddReactionDto addReactionDto = new AddReactionDto();
        addReactionDto.setReactionType("LIKE");  // Пример типа реакции

        // Преобразуем DTO в JSON
        String requestBody = objectMapper.writeValueAsString(addReactionDto);

        mockMvc.perform(post(BASE_URL + postId + "/like")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isNotFound());  // Ожидаем статус NOT FOUND (404)
    }

    @Test
    @WithMockUser(username = AUTHOR_UUID)
    void deleteLike_shouldReturnNotFoundStatus_whenPostDoesNotExist() throws Exception {
        Long postId = 999L;  // Несуществующий пост

        mockMvc.perform(delete(BASE_URL + postId + "/like"))
                .andExpect(status().isNotFound());  // Ожидаем статус NOT FOUND (404)
    }
}
