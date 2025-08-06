package com.example.cookingmanagement.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // CSRFを有効化（デフォルトで有効）
                .csrf(csrf -> csrf.ignoringRequestMatchers("/css/**", "/images/**", "/kami.mp3"))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/css/**", "/images/**", "/kami.mp3",
                                "/login", "/logout", "/register"
                        ).permitAll()
                        .requestMatchers(
                                "/recipes/new", "/recipes/edit/**",
                                "/recipes/update", "/recipes/delete/**",
                                "/recipes/**"
                        ).authenticated()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/recipes", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/")
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
