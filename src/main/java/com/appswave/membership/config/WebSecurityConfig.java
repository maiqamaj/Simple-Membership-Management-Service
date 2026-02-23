package com.appswave.membership.config;


import com.appswave.membership.util.JwtConfigProperties;
import com.appswave.membership.util.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
public class WebSecurityConfig {

    private final UserDetailsService userDetailsService;

    public WebSecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public AuthenticationManager authenticationManager(final AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(final HttpSecurity http,
                                                   final JwtConfigProperties properties,
                                                   final ObjectMapper objectMapper,
                                                   final JwtService jwtTokenService,
                                                   final UserDetailsService userDetailsService) throws Exception {

        JwtAuthenticationFilter jwtAuthenticationFilter = jwtAuthenticationFilter(userDetailsService, properties, objectMapper, jwtTokenService);

        return http.cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionManagementConfigurer -> sessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize.requestMatchers(permitAllUrls()).permitAll()
                        .anyRequest()
                        .authenticated())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(final UserDetailsService userDetailsService,
                                                           final JwtConfigProperties properties,
                                                           final ObjectMapper objectMapper,
                                                           final JwtService jwtTokenService
    ) {
        return new JwtAuthenticationFilter(userDetailsService, jwtTokenService, properties, objectMapper);
    }

    private String[] permitAllUrls() {
        return new String[]{
                "/",
                "/swagger-ui/**",
                "/swagger-ui.html",
                "/v3/api-docs/**",
                "/h2-console/**",
                "/api/v1/authentication/**"
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}