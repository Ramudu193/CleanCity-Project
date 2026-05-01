package com.cleancity.backend.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        .requestMatchers(
                                "/api/users/register",
                                "/api/users/login"
                        ).permitAll()

                        .requestMatchers("/uploads/**").permitAll()

                        // Leaderboard public
                        .requestMatchers(HttpMethod.GET, "/api/volunteers/leaderboard").permitAll()

                        // Volunteer proof submission is public
                        .requestMatchers(HttpMethod.POST, "/api/proofs/submit").permitAll()

                        // Proof review / approve / reject / delete ADMIN only
                        .requestMatchers(HttpMethod.GET, "/api/proofs").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/proofs/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/proofs/**").hasAuthority("ADMIN")

                        // Complaint APIs
                        .requestMatchers(HttpMethod.POST, "/api/complaints").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/complaints").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/complaints/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/complaints/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/complaints/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/admin/stats").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/notifications/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/notifications/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/volunteers/leaderboard").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/volunteers/**").authenticated()

                        .anyRequest().authenticated()
                )
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(List.of("http://localhost:5173"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type", "Accept"));
        config.setExposedHeaders(List.of("Authorization"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}