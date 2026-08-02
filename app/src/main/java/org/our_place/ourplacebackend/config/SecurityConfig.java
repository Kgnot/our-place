package org.our_place.ourplacebackend.config;


import org.our_place.common.security.PublicEndpoints;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Arrays;
import java.util.List;

@Configuration
public class SecurityConfig {

    private final List<PublicEndpoints> publicEndpoints;

    public SecurityConfig(List<PublicEndpoints> publicEndpoints) {
        this.publicEndpoints = publicEndpoints;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        String[] publicPaths = publicEndpoints.stream()
                .flatMap(p -> Arrays.stream(p.paths()))
                .toArray(String[]::new);

        http.authorizeHttpRequests(auth -> auth
                        .requestMatchers(publicPaths).permitAll()
                        .requestMatchers("/actuator/health", "/actuator/**").permitAll()
                        .anyRequest().authenticated()
                );
        return http.build();
    }

}
