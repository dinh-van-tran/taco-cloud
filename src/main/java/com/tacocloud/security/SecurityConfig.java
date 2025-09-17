package com.tacocloud.security;

import com.tacocloud.User;
import com.tacocloud.data.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Slf4j
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
            Mono<User> monoUser = userRepository.findByUsername(username);
            var optionUser = monoUser.blockOptional();
            if (optionUser.isEmpty()) {
                throw new UsernameNotFoundException(String.format("User '%s' not found.", username));
            }

            return optionUser.get();
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(authorize -> {
                    authorize
                            // allow access to root and static resources for all users
                            .requestMatchers("/", "/h2-console/**", "/css/**", "/js/**", "/images/**").permitAll()
                            // if url starts with /design or /orders, user must have USER role
                            .requestMatchers("/design", "/orders/**").hasAuthority("ROLE_USER")
                            // if url starts with /admin, user must have ADMIN role
                            .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
                            // allow access to root and static resources for all users
                            .requestMatchers("/").permitAll()
                            // TODO: exclude below api from OAuth2 for testing purpose, remove it
                            .requestMatchers("/api/orders/**").permitAll()
                            // other requests must be authenticated
                            .anyRequest().authenticated();
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
                // Enable OAuth2
                .oauth2ResourceServer(
                        oath2 -> oath2.jwt(
                                jwtConfigurer -> jwtConfigurer.jwtAuthenticationConverter(cognitoGroupsToRolesConverter()
                                )
                        )
                )
                .build();
    }

    private JwtAuthenticationConverter cognitoGroupsToRolesConverter() {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwt -> {
                    List<String> roles = new ArrayList<>();
                    if (jwt.getClaims().containsKey("cognito:groups")) {
                        var groups = (List<String>) jwt.getClaims().getOrDefault("cognito:groups", new ArrayList<String>());
                        roles.addAll(groups);
                    } else {
                        var scopes = ((String) jwt.getClaims().getOrDefault("scope", "")).split(" ");
                        roles.addAll(Arrays.asList(scopes));
                    }

                    return roles
                            .stream()
                            .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase(Locale.ROOT)))
                            .collect(Collectors.toSet());
                }
        );

        return jwtAuthenticationConverter;
    }
}
