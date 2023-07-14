package com.avada.myHouse24.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.security.config.Customizer.withDefaults;


@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {
    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AdminDetailsServiceImpl adminDetailsService(){return new AdminDetailsServiceImpl();}
    @Bean
    public DaoAuthenticationProvider authenticationProviderUser(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        authenticationProvider.setUserDetailsService(userDetailsService());
        return authenticationProvider;
    }
    @Bean
    public DaoAuthenticationProvider authenticationProviderAdmin(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        authenticationProvider.setUserDetailsService(adminDetailsService());
        return authenticationProvider;
    }
    @Bean
    @Order(2)
    SecurityFilterChain userSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authenticationProvider(authenticationProviderUser())
//                .securityMatcher("/cabinet/**")
                .csrf(csrf -> csrf.disable())
//                .csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/login/**", "/cabinet/login", "/admin/login/**", "/cabinet/registration", "/oauth/**").permitAll();
                    auth.requestMatchers("/favicon.ico").permitAll();
                    auth.requestMatchers("/secured").authenticated();
                    auth.anyRequest().permitAll();
                })
                .formLogin(formLogin -> formLogin.loginPage("/cabinet/login"))
                .formLogin(withDefaults())
                .oauth2Login(oauth2Login -> oauth2Login.loginPage("/cabinet/login").permitAll())
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .logoutSuccessUrl("/secured")
                )
                .build();
    }
    @Bean
    @Order(1)
    SecurityFilterChain adminSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authenticationProvider(authenticationProviderAdmin())
                .securityMatcher("/admin/**")
                .csrf(csrf -> csrf.disable())
//                .csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/login/**", "/admin/login", "/cabinet/login/**", "/cabinet/registration", "/oauth/**").permitAll();
                    auth.requestMatchers("/favicon.ico").permitAll();
                    auth.requestMatchers("/admin/role-form").hasAuthority("roles");
                    auth.anyRequest().permitAll();
                })
                .formLogin(formLogin -> formLogin.loginPage("/admin/login").permitAll())
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/admin/logout"))
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .logoutSuccessUrl("/secured")
                )
                .build();
    }
}
