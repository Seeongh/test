package com._3o3.demo.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true); //javascript 허용
        config.addAllowedOrigin("*"); //ip 허용
        config.addAllowedHeader("*"); //모든 header에 응답 허용
        config.addAllowedMethod("*"); //모든 HttpMethod 요청 허용
        source.registerCorsConfiguration("/szs/**", config);

        return new CorsFilter(source);

    }
}
