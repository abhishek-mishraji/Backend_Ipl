package com.ipl.backend.security;

import com.ipl.backend.jwt.JwtRequestFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http,
                        JwtRequestFilter jwtRequestFilter,
                        RestAuthenticationEntryPoint restAuthenticationEntryPoint,
                        RestAccessDeniedHandler restAccessDeniedHandler) throws Exception {

                http
                                .csrf(csrf -> csrf.disable())
                                .headers(headers -> headers.frameOptions(frame -> frame.disable()))
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .exceptionHandling(ex -> ex
                                                .authenticationEntryPoint(restAuthenticationEntryPoint)
                                                .accessDeniedHandler(restAccessDeniedHandler))
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers("/user/register", "/user/login", "/h2-console/**")
                                                .permitAll()

                                                .requestMatchers(HttpMethod.POST, "/team/**").hasRole("ADMIN")
                                                .requestMatchers(HttpMethod.PUT, "/team/**").hasRole("ADMIN")
                                                .requestMatchers(HttpMethod.DELETE, "/team/**").hasRole("ADMIN")
                                                .requestMatchers(HttpMethod.GET, "/team/**").hasAnyRole("USER", "ADMIN")

                                                .requestMatchers(HttpMethod.POST, "/cricketer/**").hasRole("ADMIN")
                                                .requestMatchers(HttpMethod.PUT, "/cricketer/**").hasRole("ADMIN")
                                                .requestMatchers(HttpMethod.DELETE, "/cricketer/**").hasRole("ADMIN")
                                                .requestMatchers(HttpMethod.GET, "/cricketer/**")
                                                .hasAnyRole("USER", "ADMIN")

                                                .anyRequest().authenticated())
                                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }
}