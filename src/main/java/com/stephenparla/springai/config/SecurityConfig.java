package com.stephenparla.springai.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stephenparla.springai.util.ChatConstants;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${user.name}")
    private String username;

    @Value("${user.password}")
    private String password;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/aichat/ping").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .successHandler((request, response, authentication) -> {
                            response.setStatus(HttpServletResponse.SC_OK);
                            response.setContentType("application/json");

                            Map<String, Object> data = new HashMap<>();
                            data.put("message", "Login successful!");
                            data.put("username", authentication.getName());
                            response.getOutputStream().println(objectMapper.writeValueAsString(data));
                        })
                        .failureHandler((request, response, exception) -> {
                            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        }))
                .csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        // Example of an in-memory user store
        UserDetails user = User.builder()
                .username(username)
                .password(passwordEncoder().encode(password))
                .roles(ChatConstants.USER)
                .build();
        return new InMemoryUserDetailsManager(user); //
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Use BCrypt as the password encoder
    }
}
