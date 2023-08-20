package com.mc_website.apigateway.utils.Configuration;

import com.mc_website.apigateway.security.CustomBasicAuthenticationEntryPoint;
import com.mc_website.apigateway.security.JwtTokenFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;
@Configuration
@EnableWebSecurity
public class SecurityConfig  {


    UserDetailsService userDetailService;
    JwtTokenFilter jwtTokenFilter;
    CustomBasicAuthenticationEntryPoint customBasicAuthenticationEntryPoint;

    public SecurityConfig(UserDetailsService userDetailService, JwtTokenFilter jwtTokenFilter, CustomBasicAuthenticationEntryPoint customBasicAuthenticationEntryPoint) {
        this.userDetailService = userDetailService;
        this.jwtTokenFilter = jwtTokenFilter;
        this.customBasicAuthenticationEntryPoint = customBasicAuthenticationEntryPoint;
    }

    @Bean
    public AuthenticationManager authenticationManager(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailService);
        authProvider.setPasswordEncoder(encoder());
        return new ProviderManager(authProvider);
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers(HttpMethod.POST,"/api/v1/users/login").permitAll()
                        .requestMatchers(HttpMethod.POST,"/api/v1/users").permitAll()
                        .requestMatchers("/api/v1/users/forgot_password").permitAll()
                        .requestMatchers("/api/v1/users/reset_password").permitAll()
                        .requestMatchers("/api/v1/users/**").authenticated()
                        .anyRequest().denyAll())
                .logout(logout -> logout
                        .logoutUrl("/api/v1/users/logout")
                        .logoutSuccessHandler((httpServletRequest, httpServletResponse, authentication) -> {
                            httpServletResponse.setStatus(HttpServletResponse.SC_NO_CONTENT);
                        })
                        .addLogoutHandler((request, response, auth) -> {
                            for (Cookie cookie : request.getCookies()) {
                                    String cookieName = cookie.getName();
                                    Cookie cookieToDelete = new Cookie(cookieName, null);
                                    cookieToDelete.setMaxAge(0);
                                    response.addCookie(cookieToDelete);
                            }
                        }))
                .httpBasic()
                .authenticationEntryPoint(customBasicAuthenticationEntryPoint)
                .and()
                .csrf().disable()
                .cors().configurationSource(corsConfigurationSource());
        http.addFilterBefore(
                jwtTokenFilter,
                UsernamePasswordAuthenticationFilter.class
        );

        return http.build();
    }

//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        return (web) -> web.ignoring()
//                .requestMatchers(HttpMethod.POST,"/api/v1/users/login")
//                .requestMatchers(HttpMethod.POST,"/api/v1/users");
//    }

@Bean
public CorsConfigurationSource corsConfigurationSource() {
    final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    final CorsConfiguration config = new CorsConfiguration();
    config.setAllowCredentials(true);
    config.addAllowedOrigin("http://localhost:3000");
    config.addAllowedMethod("GET");
    config.addAllowedMethod("PUT");
    config.addAllowedMethod("POST");
    config.addAllowedMethod("DELETE");
    config.addAllowedHeader("*");
    source.registerCorsConfiguration("/**", config);
    return source;
}
    @Bean
    public UserDetailsService userDetailsServiceBean() throws Exception {

        UserDetails user = User.withUsername("user")
                .password(encoder().encode("userPass"))
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(user);}
    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
}
