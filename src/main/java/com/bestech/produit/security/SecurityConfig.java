package com.bestech.produit.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Collections;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain (HttpSecurity http) throws Exception {
        http.sessionManagement( session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource( request -> {
                    CorsConfiguration corsConfig  = new CorsConfiguration();
                    corsConfig .setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
                    corsConfig .setAllowedMethods(Collections.singletonList("*"));
                    corsConfig .setAllowedHeaders(Collections.singletonList("*"));
                    corsConfig .setExposedHeaders(Collections.singletonList("Authorization"));
                    return corsConfig ;
                }))
                .authorizeHttpRequests( auth -> auth
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/produit/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/api/produit/**").hasRole("ADMIN")
                        .requestMatchers("/api/categorie/**").hasRole("ADMIN")
                        .anyRequest().authenticated() )
                .addFilterBefore(new JWTAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
