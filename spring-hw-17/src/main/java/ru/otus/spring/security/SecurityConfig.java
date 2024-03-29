package ru.otus.spring.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collections;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf().disable()
                .headers().frameOptions().disable()
                .and()
                .authorizeRequests(auth -> auth
                        .antMatchers("/datarest/**").permitAll()
                        .antMatchers("/actuator/**").permitAll()
                        .antMatchers("/").permitAll()
                        .antMatchers("/api/books/*").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin()
                .and()
                .anonymous().principal(new User("anonymous", "", Collections.emptyList()))
                .and()
                .logout().logoutSuccessUrl("/")
                .and()
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
