package com.sw2parcial.backend.config;

import com.sw2parcial.backend.filter.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // Permitir TODO
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /*
     * @Bean
     * public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
     * http
     * .csrf(csrf -> csrf.disable())
     * .sessionManagement(session ->
     * session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
     * .authorizeHttpRequests(auth -> auth
     * .requestMatchers("/api/auth/**").permitAll()
     * .requestMatchers("/graphql", "/graphiql", "/vendor/**", "/playground",
     * "/voyager").permitAll() // GraphQL sin auth
     * .requestMatchers("/api/roles/**", "/api/permisos/**").hasRole("ADMIN")
     * .requestMatchers("/api/tipos/**").authenticated()
     * .requestMatchers("/api/productos/**").authenticated()
     * .requestMatchers("/api/ventas/**").authenticated()
     * .requestMatchers("/api/precios/**").authenticated()
     * .anyRequest().authenticated()
     * )
     * .addFilterBefore(jwtAuthenticationFilter,
     * UsernamePasswordAuthenticationFilter.class);
     * 
     * return http.build();
     * }
     */
}