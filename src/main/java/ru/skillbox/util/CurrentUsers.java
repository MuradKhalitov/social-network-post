package ru.skillbox.util;

//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CurrentUsers {
    private final JwtTokenUtil jwtTokenUtil;
@Autowired
    public CurrentUsers(JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }


//        public static String getCurrentUsername() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication != null && authentication.isAuthenticated()) {
//            Object principal = authentication.getPrincipal();
//            if (principal instanceof UserDetails) {
//                return ((UserDetails) principal).getUsername();
//            } else {
//                // Вернуть имя пользователя напрямую, если UserDetails не используется
//                return principal.toString();
//            }
//        }
//        return null;
//    }
//        public static boolean hasRole(String role) {
//        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
//                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(role));
//    }

    public UUID getCurrentUserId() {
        return jwtTokenUtil.getUserIdFromToken();//"tagir";
    }

//    public String getCurrentUsername() {
//        return jwtTokenUtil.getUsernameFromToken();//"tagir";
//    }

    public boolean hasRole(String role) {
        return (jwtTokenUtil.getRoleFromToken().equals(role));//true;
    }
}
