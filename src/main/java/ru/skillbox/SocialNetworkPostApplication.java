package ru.skillbox;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SocialNetworkPostApplication {

    public static void main(String[] args) {
        SpringApplication.run(SocialNetworkPostApplication.class, args);
    }

//    @Bean
//    CommandLineRunner run(UserService userService) {
//        return args -> {
//            userService.createUser(new User(null, "Murad", "darggun@gmail,com", "111"));
//            userService.createUser(new User(null, "Roma", "roma@gmail,com", "111"));
//            userService.createUser(new User(null, "Rusik", "rusik@gmail,com", "111"));
//        };
//    }

}
