package ru.skillbox.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import ru.skillbox.client.OpenFeignClient;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

 class JwtTokenFilterTest {

    @Mock
    private OpenFeignClient openFeignClient;

    @InjectMocks
    private JwtTokenFilter jwtTokenFilter;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private FilterChain filterChain;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        filterChain = mock(FilterChain.class);
        SecurityContextHolder.clearContext();
    }

    @Test
    void doFilterInternal_validToken_shouldAuthenticate() throws ServletException, IOException {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwiaWQiOiIxMDAwMDAwMC0wMDAwLTAwMDAtMDAwMC0wMDAwMDAwMDAyMDAiLCJleHAiOjE3Mjg4MTg1NDYsImlhdCI6OTcyODczMjE0Nn0.uSqSdG6P0m9ZpV0HLPqJu-ttHIaYB7lO7oM6o-lvd6A";
        String accountId = "10000000-0000-0000-0000-000000000200";
        String authHeader = "Bearer " + token;

        request.addHeader("Authorization", authHeader);

        when(openFeignClient.validateToken(authHeader)).thenReturn(true);

        jwtTokenFilter.doFilterInternal(request, response, filterChain);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals(accountId, SecurityContextHolder.getContext().getAuthentication().getName());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_invalidToken_shouldNotAuthenticate() throws ServletException, IOException {
        String token = "invalidToken";
        String authHeader = "Bearer " + token;
        request.addHeader("Authorization", authHeader);

        when(openFeignClient.validateToken(authHeader)).thenReturn(false);

        jwtTokenFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_missingToken_shouldNotAuthenticate() throws ServletException, IOException {
        jwtTokenFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void getIdFromToken_validToken_shouldReturnId() {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwiaWQiOiIxMDAwMDAwMC0wMDAwLTAwMDAtMDAwMC0wMDAwMDAwMDAyMDAiLCJleHAiOjE3Mjg4MTg1NDYsImlhdCI6OTcyODczMjE0Nn0.uSqSdG6P0m9ZpV0HLPqJu-ttHIaYB7lO7oM6o-lvd6A";
        String expectedId = "10000000-0000-0000-0000-000000000200";

        String actualId = jwtTokenFilter.getIdFromToken(token);
        assertEquals(expectedId, actualId);
    }

    @Test
    void getIdFromToken_invalidToken_shouldThrowException() {
        String invalidToken = "invalidToken";

        assertThrows(RuntimeException.class, () -> jwtTokenFilter.getIdFromToken(invalidToken));
    }

    @Test
    void getRolesFromToken_validTokenWithRoles_shouldReturnRoles() {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwiaWQiOiIxMDAwMDAwMC0wMDAwLTAwMDAtMDAwMC0wMDAwMDAwMDAyMDAiLCJleHAiOjE3Mjg4MTg1NDYsImlhdCI6OTcyODczMjE0Nn0.uSqSdG6P0m9ZpV0HLPqJu-ttHIaYB7lO7oM6o-lvd6A";

        List<?> authorities = jwtTokenFilter.getRolesFromToken(token);

        assertEquals(1, authorities.size());
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_USER")));
    }

    @Test
    void getRolesFromToken_tokenWithoutRoles_shouldReturnDefaultRole(){
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJpZCI6IjEwMDAwMDAwLTAwMDAtMDAwMC0wMDAwLTAwMDAwMDAwMDIwMCIsImV4cCI6MTcyODgxODU0NiwiaWF0Ijo5NzI4NzMyMTQ2fQ.jy_X-lwg3gPNlB6UllxOvaEk1oF7GKD24nSCEcndHSA";

        List<?> authorities = jwtTokenFilter.getRolesFromToken(token);

        assertEquals(1, authorities.size());
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_USER")));
    }
}
