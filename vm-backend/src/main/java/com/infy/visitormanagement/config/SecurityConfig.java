package com.infy.visitormanagement.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http)throws Exception {
        http
                .cors(cors->{})
                .csrf(crsf->crsf.disable())
                .formLogin(form->form.disable())
                .httpBasic(basic->basic.disable())
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .authorizeHttpRequests(auth->auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers("/gate/**").hasAnyRole("ADMIN","RECEPTIONIST","SECURITY")
                        .requestMatchers("/api/**").hasAnyRole("ADMIN","RECEPTIONIST","SECURITY")
                        .requestMatchers("/feedback/**").permitAll()
                        .anyRequest().authenticated()
                );
        return http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}