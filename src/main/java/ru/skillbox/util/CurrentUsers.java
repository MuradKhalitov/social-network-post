package ru.skillbox.util;

//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;

public class CurrentUsers {
//    public static String getCurrentUsername() {
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

    public static String getCurrentUsername() {
     return "admin";
    }
    public static boolean hasRole(String role) {
        return true;
    }

//    public static boolean hasRole(String role) {
//        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
//                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(role));
//    }
}
