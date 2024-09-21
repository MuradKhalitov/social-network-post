package ru.skillbox.security;

import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenFilter extends OncePerRequestFilter {
    @Value("${app.jwt.uriValidate}")
    private String uriValidate;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String headerAuth = request.getHeader(HttpHeaders.AUTHORIZATION);
            String token = null;
            if (org.springframework.util.StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
                token = headerAuth.substring(7);
            }
            if (token != null &&
                    //true){
                    validateToken(headerAuth)) {
                String accountId = getIdFromToken(token);
                if (accountId != null && !accountId.isEmpty()) {
                    List<SimpleGrantedAuthority> authorities = getRolesFromToken(token);
                    System.out.println(authorities);
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            new User(accountId, "", authorities),
                            null,
                            authorities
                    );
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
        } catch (Exception e) {
            log.error("Cannot set user authentication: {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    public static Boolean validateToken(String headerAuth) throws io.jsonwebtoken.io.IOException, InterruptedException, java.io.IOException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://89.111.174.153:9090/api/v1/auth/tokenValidation"))
                .header("Authorization", headerAuth)
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return response.body().trim().equals("true");
        } else {
            throw new RuntimeException("Failed to validate token: " + response.statusCode());
        }
    }

    public String getIdFromToken(String token) {
        try {
            String sub = SignedJWT.parse(token).getPayload().toJSONObject().get("id").toString();
            System.out.println(sub);
            return sub;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public List<SimpleGrantedAuthority> getRolesFromToken(String token) {
        try {
            var payload = SignedJWT.parse(token).getPayload().toJSONObject();
            if (payload.get("roles") == null) {
                return List.of(new SimpleGrantedAuthority("ROLE_USER"));
            }
            List<String> roles = (List<String>) payload.get("roles");
            return roles.stream()
                    .map(role -> new SimpleGrantedAuthority(role))
                    .collect(Collectors.toList());
        } catch (ParseException e) {
            throw new RuntimeException("Failed to parse JWT token", e);
        }
    }
}
