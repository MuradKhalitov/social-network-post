package ru.skillbox.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import ru.skillbox.client.AccountFeignClient;
import ru.skillbox.client.FriendsFeignClient;
import ru.skillbox.dto.AccountDto;
import ru.skillbox.dto.AccountSearchDto;
import ru.skillbox.dto.kafka.BotPost;
import ru.skillbox.dto.kafka.NotificationPost;
import ru.skillbox.dto.post.request.PostDto;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PostControllerTestClass extends BaseTestClass {

    @MockBean
    private AccountFeignClient accountFeignClient;
    @MockBean
    private FriendsFeignClient friendsFeignClient;
    @MockBean
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Test
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
        postDto.setPostText("This is a test post.");

        when(accountFeignClient.getAccountById(UUID.fromString(AUTHOR_UUID)))
                .thenReturn(new AccountDto("John", "Doe"));

        when(kafkaTemplate.send(eq("notification-topic"), any(NotificationPost.class)))
                .thenReturn(CompletableFuture.completedFuture(null));

        when(kafkaTemplate.send(eq("bot-topic"), any(BotPost.class)))
                .thenReturn(CompletableFuture.completedFuture(null));

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2L))
                .andExpect(jsonPath("$.title").value("Test Post"));


        verify(kafkaTemplate, times(1)).send(eq("notification-topic"), any(NotificationPost.class));
        verify(kafkaTemplate, times(1)).send(eq("bot-topic"), any(BotPost.class));
    }

    @Test
    void createPost_shouldReturnUnauthorized() throws Exception {
        PostDto postDto = new PostDto();
        postDto.setTitle("Test Post");
        postDto.setPostText("This is a test post.");

        when(accountFeignClient.getAccountById(UUID.fromString(AUTHOR_UUID)))
                .thenReturn(new AccountDto("John", "Doe"));

        when(kafkaTemplate.send(eq("notification-topic"), any(NotificationPost.class)))
                .thenReturn(CompletableFuture.completedFuture(null));

        when(kafkaTemplate.send(eq("bot-topic"), any(BotPost.class)))
                .thenReturn(CompletableFuture.completedFuture(null));

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postDto)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(HttpServletResponse.SC_UNAUTHORIZED))
                .andExpect(jsonPath("$.error").value("Unauthorized"))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @WithMockUser(username = AUTHOR_UUID)
    void updatePost_shouldReturnCreatedStatus() throws Exception {
        Long id = 1L;
        PostDto postDto = new PostDto();
        postDto.setId(id);
        postDto.setTitle("Updated Post");

        mockMvc.perform(put(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postDto)))
                .andExpect(status().isCreated());
    }

    @Test
    void updatePost_shouldReturnUnauthorized() throws Exception {
        Long id = 1L;
        PostDto postDto = new PostDto();
        postDto.setId(id);
        postDto.setTitle("Updated Post");

        mockMvc.perform(put(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postDto)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(HttpServletResponse.SC_UNAUTHORIZED))
                .andExpect(jsonPath("$.error").value("Unauthorized"))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @WithMockUser(username = AUTHOR_UUID)
    void deletePost_shouldReturnOkStatus() throws Exception {
        Long id = 1L;

        mockMvc.perform(delete(BASE_URL + "/{id}", id))
                .andExpect(status().isOk());
    }

    @Test
    void deletePost_shouldReturnUnauthorized() throws Exception {
        Long id = 1L;

        mockMvc.perform(delete(BASE_URL + "/{id}", id))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(HttpServletResponse.SC_UNAUTHORIZED))
                .andExpect(jsonPath("$.error").value("Unauthorized"))
                .andExpect(jsonPath("$.message").exists());
    }


    @Test
    @WithMockUser(username = AUTHOR_UUID)
    void deletePost_shouldReturnNotFound() throws Exception {
        Long id = 999L;

        mockMvc.perform(delete(BASE_URL + "/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = AUTHOR_UUID)
    void searchPosts_shouldReturnOkStatus() throws Exception {
        UUID friendUUID = UUID.fromString("10000000-0000-0000-0000-000000000300");

        when(friendsFeignClient.getFriendsIds(UUID.fromString(AUTHOR_UUID)))
                .thenReturn(List.of(friendUUID));
        AccountDto accountDto = new AccountDto();
        accountDto.setId(UUID.fromString(AUTHOR_UUID));
        AccountDto accountDto1 = new AccountDto();
        accountDto1.setId(friendUUID);

        when(accountFeignClient.searchAccount(any(AccountSearchDto.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(accountDto, accountDto1)));

        mockMvc.perform(get(BASE_URL)
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "id,asc")
                        .param("isDeleted", "false")
                        .param("withFriends", "true"))
                .andExpect(status().isOk());
    }


    @Test
    void searchPosts_shouldReturnUnauthorized() throws Exception {
        mockMvc.perform(get(BASE_URL)
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "id,asc")
                        .param("isDeleted", "false"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(HttpServletResponse.SC_UNAUTHORIZED))
                .andExpect(jsonPath("$.error").value("Unauthorized"))
                .andExpect(jsonPath("$.message").exists());
    }
}

