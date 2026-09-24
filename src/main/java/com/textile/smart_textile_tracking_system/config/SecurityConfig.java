package com.textile.smart_textile_tracking_system.config;

import com.textile.smart_textile_tracking_system.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(customUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authenticationProvider(authenticationProvider())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/login", "/register", "/forgot-password",
                                 "/reset-password", "/css/**", "/js/**", "/images/**").permitAll()
                .requestMatchers("/owner-dashboard", "/owner-orders", "/owner-search",
                                 "/create-order", "/save-order", "/reports",
                                 "/workers", "/search-stock",
                                 "/reports/export/**").hasRole("OWNER")
                // Worker role covers the remaining worker module pages; Stock was removed
                // from the Worker module, so no stock routes are worker-accessible.
                .requestMatchers("/worker-dashboard", "/worker-orders",
                                 "/tasks", "/production-orders").hasRole("WORKER")
                // Every worker module page/action lives under /worker (my-tasks, notifications,
                // task status updates) and must stay worker-only.
                .requestMatchers("/worker/**").hasRole("WORKER")
                .requestMatchers("/owner/**").hasRole("OWNER")
                .requestMatchers("/admin/**").hasRole("ADMIN")
                // The REST API manages workers, tasks, production orders and reports,
                // so it is restricted to administrators instead of any logged-in user.
                .requestMatchers("/api/**").hasRole("ADMIN")
                .requestMatchers("/profile", "/update-profile", "/change-password").authenticated()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .usernameParameter("username")
                .passwordParameter("password")
                .successHandler((request, response, authentication) -> {
                    var authorities = authentication.getAuthorities();
                    String role = authorities.iterator().next().getAuthority();
                    if ("ROLE_OWNER".equals(role)) {
                        response.sendRedirect("/owner-dashboard");
                    } else if ("ROLE_WORKER".equals(role)) {
                        response.sendRedirect("/worker-dashboard");
                    } else if ("ROLE_ADMIN".equals(role)) {
                        response.sendRedirect("/admin/dashboard");
                    } else {
                        response.sendRedirect("/");
                    }
                })
                .failureUrl("/login?error=true")
                .permitAll()
            )
            .rememberMe(remember -> remember
                .key("trizen-remember-me-key")
                .tokenValiditySeconds(14 * 24 * 60 * 60)
                .userDetailsService(customUserDetailsService)
            )
            .logout(logout -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "POST"))
                .logoutSuccessUrl("/login?logout=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID", "remember-me")
                .permitAll()
            )
            .sessionManagement(session -> session
                .maximumSessions(1)
                .expiredUrl("/login?expired=true")
            );

        return http.build();
    }
}
