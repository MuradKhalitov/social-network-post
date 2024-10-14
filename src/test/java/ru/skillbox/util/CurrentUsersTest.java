package ru.skillbox.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CurrentUsersTest {

    private CurrentUsers currentUsers;
    private SecurityContext securityContext;
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        currentUsers = new CurrentUsers();
        securityContext = mock(SecurityContext.class);
        authentication = mock(Authentication.class);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void getCurrentUserId_success() {
        // Успешное получение UUID, когда пользователь аутентифицирован
        String validUUID = UUID.randomUUID().toString();
        User user = new User(validUUID, "password", Collections.emptyList());

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(user);

        UUID result = currentUsers.getCurrentUserId();

        assertEquals(UUID.fromString(validUUID), result);
    }

    @Test
    void getCurrentUserId_notAuthenticated() {
        // Исключение, когда пользователь не аутентифицирован
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> currentUsers.getCurrentUserId());
        assertEquals("User is not authenticated", exception.getMessage());
    }

    @Test
    void getCurrentUserId_invalidUUID() {
        // Исключение, когда userId не является валидным UUID
        User user = new User("invalid-uuid", "password", Collections.emptyList());

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(user);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> currentUsers.getCurrentUserId());
        assertTrue(exception.getMessage().contains("Invalid UUID format for user ID"));
    }

    @Test
    void hasRole_noRole() {
        // Проверка, когда роль отсутствует
        String role = "ROLE_USER";
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getAuthorities()).thenReturn(Collections.emptyList());

        boolean result = currentUsers.hasRole(role);
        assertFalse(result);
    }

    @Test
    void hasRole_notAuthenticated() {
        // Проверка, когда пользователь не аутентифицирован
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(false);

        boolean result = currentUsers.hasRole("ROLE_USER");
        assertFalse(result);
    }
}

