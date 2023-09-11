package com.using.you.are.version.spring.which.config;

import com.using.you.are.version.spring.which.login.CustomUserDetailsService;
import com.using.you.are.version.spring.which.login.OAuth2UserDetailService;
import com.using.you.are.version.spring.which.utils.UtilsPasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;
    private final PasswordEncoder utilsPasswordEncoder;
    private final OAuth2UserDetailService oauth2UserService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        http.authorizeRequests()
                .requestMatchers("/user/**").authenticated()
                .requestMatchers("/user/**").access("hasAnyRole('ROLE_MEMBER') or hasRole('ROLE_OAUTH') or hasRole('ROLE_ADMIN')or hasRole('ROLE_MANAGER')or hasRole('ROLE_GUEST')")
                .requestMatchers("/oauth/**").access("hasAnyRole('ROLE_OAUTH')")
                .requestMatchers("/private/**").access("hasAnyRole('ROLE_MEMBER') or hasRole('ROLE_OAUTH') or hasRole('ROLE_ADMIN')or hasRole('ROLE_MANAGER')")
                .requestMatchers("/manager/**").access("hasAnyRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
                .requestMatchers("/admin/**").access("hasAnyRole('ROLE_ADMIN')")
                .requestMatchers("/","/logout").permitAll()
                .and().formLogin(formLogin -> formLogin
                        .loginPage("/loginForm")
                        .loginProcessingUrl("/login")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/after-login")
                        .failureUrl("/fail")
                ).oauth2Login(oauth -> oauth.loginPage("/loginForm")
                        .defaultSuccessUrl("/after-login")
                        .failureUrl("/fail")
                        .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig.userService(oauth2UserService))  //oauth 잘못된 uri 리다이렉션 127.0.0.1 인지 localhost 인지 확인할것
                );
        return http.build();
    }


}
