package ru.otus.spring.security;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collections;

@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf().disable()
                .authorizeRequests(auth -> auth
                        .antMatchers("/").permitAll()
                        .antMatchers("/**").authenticated()
                )
                .formLogin()
                .and()
                .anonymous().principal(new User("anonymous", "", Collections.emptyList()))
                .and()
                .logout().logoutSuccessUrl("/")
                .and()
                .build();
    }
}
