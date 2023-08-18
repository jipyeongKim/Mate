package com.mate.kosmo.config;

import com.mate.kosmo.config.filter.JwtAuthorizationFilter;
import com.mate.kosmo.config.filter.MateAuthenticationFilter;
import com.mate.kosmo.config.handler.MateAuthFailureHandler;
import com.mate.kosmo.config.handler.MateAuthSuccessHandler;
import com.mate.kosmo.config.handler.MateAuthenticationProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Slf4j
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers((PathRequest.toStaticResources().atCommonLocations()));
    }// webSecurityCustomizer

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .cors().configurationSource(corsConfigurationSource())
                .and()
                .formLogin().disable()
                .httpBasic().disable()
                .authorizeHttpRequests((auth) -> auth.anyRequest().permitAll())
                .addFilterBefore(jwtAuthorizationFilter(), BasicAuthenticationFilter.class)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .oauth2Login()
                .and()
                .logout().disable()
                .addFilterBefore(mateAuthenticationFilter(),UsernamePasswordAuthenticationFilter.class);

        return http.build();

    }// securityFilterChain

    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setAllowedOrigins(List.of("http://localhost:3000/"));
        corsConfiguration.setAllowedMethods(List.of("GET","POST","PUT","DELETE","PATCH","OPTIONS"));
        corsConfiguration.setAllowedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**",corsConfiguration);

        return urlBasedCorsConfigurationSource;

    }// corsConfigurationSource


    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(mateAuthenticationProvider());
    }// authenticationManager

    @Bean
    public MateAuthenticationProvider mateAuthenticationProvider() {
        return new MateAuthenticationProvider(bCryptPasswordEncoder());
    }// mateAuthenticationProvider

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }// bCryptPasswordEncoder

    @Bean
    public MateAuthenticationFilter mateAuthenticationFilter() {
        MateAuthenticationFilter mateAuthenticationFilter = new MateAuthenticationFilter(authenticationManager());
        mateAuthenticationFilter.setFilterProcessesUrl("/spring/users/security/login");
        mateAuthenticationFilter.setAuthenticationSuccessHandler(mateAuthSuccessHandler());
        mateAuthenticationFilter.setAuthenticationFailureHandler(mateAuthFailureHandler());
        mateAuthenticationFilter.afterPropertiesSet();

        return mateAuthenticationFilter;
    }// mateAuthenticationFilter

    @Bean
    public MateAuthSuccessHandler mateAuthSuccessHandler() {
        return new MateAuthSuccessHandler();
    }// mateAuthSuccessHandler

    @Bean
    public MateAuthFailureHandler mateAuthFailureHandler() {
        return new MateAuthFailureHandler();
    }// mateAuthFailureHandler

    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() {
        return new JwtAuthorizationFilter();
    }// JwtAuthorizationFilter

}// WebSecurityConfig