package com.ecommerce.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http

            // Disable CSRF since the application will expose a stateless REST API.
            // CSRF protection will not be required when JWT authentication is introduced.
            .csrf(AbstractHttpConfigurer::disable)

            // Disable Spring Security's default login page.
            .formLogin(AbstractHttpConfigurer::disable)

            // Disable HTTP Basic authentication.
            .httpBasic(AbstractHttpConfigurer::disable)

            // Temporarily allow all requests during Phase 1.
            // This will be replaced with endpoint-specific authorization
            // once JWT authentication is implemented in Phase 2.
            .authorizeHttpRequests(authorize -> authorize
                    .anyRequest().permitAll()
            );

        return http.build();
    }
}