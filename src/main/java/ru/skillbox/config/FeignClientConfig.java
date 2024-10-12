package ru.skillbox.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

@Configuration
public class FeignClientConfig {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate requestTemplate) {
                // Получаем текущую аутентификацию
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

                if (authentication != null && authentication.isAuthenticated()) {
                    // Получаем токен из UserDetails (или иного объекта)
                    if (authentication.getPrincipal() instanceof User) {
                        User user = (User) authentication.getPrincipal();
                        // Например, если токен хранится как имя пользователя (или по-другому)
                        String token = user.getUsername(); // Здесь зависит от того, как хранится токен

                        // Добавляем токен в заголовки запроса
                        requestTemplate.header("Authorization", "Bearer " + token);
                    }
                }
            }
        };
    }
}

