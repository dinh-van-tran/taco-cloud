package com.example.tacocloud.security;

import com.example.tacocloud.User;
import com.example.tacocloud.data.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return username -> {
            User user = userRepository.findByUsername(username);
            if (user != null) {
                return user;
            }

            throw new UsernameNotFoundException(String.format("User '%s' not found.", username));
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(authorize -> {
                    authorize
                            // if url starts with /design or /orders, user must have USER role
                            .requestMatchers("/design", "/orders/**").hasAuthority("ROLE_USER")
                            // if url starts with /admin, user must have ADMIN role
                            .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
                            // allow access to all other URLs
                            .anyRequest().permitAll();
                })
                .formLogin(form -> form
                        // replace default login page with custom one
                        .loginPage("/login")
                        .defaultSuccessUrl("/design")
                        .permitAll()
                )
                .logout(option -> option.logoutSuccessUrl("/"))

                // Make H2-Console non-secured; for debug purposes
                .csrf(option -> option.ignoringRequestMatchers("/h2-console/**"))

                // Allow pages to be loaded in frames from the same origin; needed for H2-Console
                .headers(headers -> {
                    headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin);
                })
                .build();
    }
}
