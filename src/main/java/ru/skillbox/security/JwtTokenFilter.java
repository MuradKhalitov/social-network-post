package ru.skillbox.security;

import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.skillbox.client.OpenFeignClient;
import ru.skillbox.exception.InvalidTokenException;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

@Component
@Slf4j
public class JwtTokenFilter extends OncePerRequestFilter {

    private final OpenFeignClient openFeignClient;

    @Autowired
    public JwtTokenFilter(OpenFeignClient openFeignClient) {
        this.openFeignClient = openFeignClient;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String headerAuth = request.getHeader(HttpHeaders.AUTHORIZATION);
            String token = null;
            if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
                token = headerAuth.substring(7);
            }
            if (token != null && openFeignClient.validateToken(headerAuth)) {
                String accountId = getIdFromToken(token);
                if (accountId != null && !accountId.isEmpty()) {
                    List<SimpleGrantedAuthority> authorities = getRolesFromToken(token);
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            new User(accountId, "", authorities),
                            token,
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

    public String getIdFromToken(String token) {
        try {
            return SignedJWT.parse(token).getPayload().toJSONObject().get("id").toString();
        } catch (ParseException e) {
            throw new InvalidTokenException("Не удалось проанализировать токен", e);
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
                    .map(SimpleGrantedAuthority::new)
                    .toList();
        } catch (ParseException e) {
            throw new InvalidTokenException("Не удалось проанализировать токен", e);
        }
    }
}
