package com.example.tacocloud.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.ArrayList;
import java.util.List;

@Configuration
class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        List<UserDetails> usersList = new ArrayList<>();

        // add a new user with username "buzz" and password "password"
        usersList.add(new User(
                "buzz",
                passwordEncoder.encode("password"),
                List.of(
                        new SimpleGrantedAuthority("ROLE_USER")
                )
        ));

        // add a new user with username "woody" and password "password"
        usersList.add(new User(
                "woody",
                passwordEncoder.encode("password"),
                List.of(
                        new SimpleGrantedAuthority("ROLE_USER")
                )
        ));

        // in-memory user details manager
        return new InMemoryUserDetailsManager(usersList);
    }
}
