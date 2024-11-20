package com.example.ordermanagement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // contentSecurityConfig(http); to do

        // Define AntPathRequestMatcher for the routes you want to permit
        // AntPathRequestMatcher[] requestMatchers = getAntPathRequestMatchers();

        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        // Permit all on these paths
                        .requestMatchers(new AntPathRequestMatcher("/api/customers")).permitAll()
                        // All other requests must be authenticated
                        .anyRequest().authenticated()
                )
                .httpBasic(withDefaults());

        return http.build();
    }

    // Define any custom security configurations, such as CORS, headers, etc.
    private void contentSecurityConfig(HttpSecurity http) throws Exception {
        // Example of adding custom headers or content security policies, if needed
        http
                .headers((header) -> header
                        .contentTypeOptions(withDefaults())
                        .xssProtection(withDefaults())
                        .cacheControl(withDefaults())
                        .frameOptions(withDefaults())
                );
    }

    // Define the path matchers (for specific APIs you want to make publicly available)
    private AntPathRequestMatcher[] getAntPathRequestMatchers() {
        return new AntPathRequestMatcher[] {
                new AntPathRequestMatcher("/api/customers", HttpMethod.POST.name())
        };
    }
}
