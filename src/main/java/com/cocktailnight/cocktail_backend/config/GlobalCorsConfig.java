//package com.cocktailnight.cocktail_backend.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.Ordered;
//import org.springframework.core.annotation.Order;
//import org.springframework.web.servlet.config.annotation.*;
//
//@Configuration
//public class GlobalCorsConfig implements WebMvcConfigurer {
//
//    @Bean
//    @Order(Ordered.HIGHEST_PRECEDENCE)
//    public CorsFilter corsFilter() {
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        CorsConfiguration config = new CorsConfiguration();
//        config.setAllowCredentials(true); // Allows credentials to be shared
//        config.addAllowedOriginPattern("*"); // Allows any origin
//        config.addAllowedHeader("*"); // Allows any header
//        config.addAllowedMethod("*"); // Allows any method (GET, POST, etc.)
//
//        source.registerCorsConfiguration("/**", config);
//        return new CorsFilter(source);
//    }
//}
