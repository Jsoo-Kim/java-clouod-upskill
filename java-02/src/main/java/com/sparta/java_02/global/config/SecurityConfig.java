package com.sparta.java_02.global.config;

import static org.springframework.security.config.Customizer.withDefaults;

import com.sparta.java_02.global.filter.AuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

//  private static final String[] SECURITY_EXCLUDE_PATHS = {
//      "/public/**", "/api/swagger-ui/**", "/swagger-ui/**", "/swagger-ui.html",
//      "/api/v3/api-docs/**", "/v3/api-docs/**", "/favicon.ico", "/actuator/**",
//      "/swagger-resources/**", "/external/**", "/api/auth/**", "/api/users/availability",
//      "/api/users",
//  };

  private static final String[] SECURITY_EXCLUDE_PATHS = {
      "/public/**", "/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**",
      "/favicon.ico", "/actuator/**", "/swagger-resources/**", "/external/**",
      "/api/auth/**"
  };

  private final AuthenticationFilter authenticationFilter;

  public SecurityConfig(AuthenticationFilter authenticationFilter) {
    this.authenticationFilter = authenticationFilter;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http)
      throws Exception { // 여기로 들어오는 것들을 검증
    http
        .cors(withDefaults())
        .csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(authorize -> authorize
            .requestMatchers(SECURITY_EXCLUDE_PATHS).permitAll()
            .requestMatchers("/api/user").hasRole(
                "USER") // USER라는 role을 갖고 있어야 /api/user에 접근 가능! (원래 "ROLE_USER"이 풀네임인데 여기선 "ROLE_" 생략 가능)
            .requestMatchers("/api/admin").hasRole("ADMIN")
            .anyRequest().authenticated()
        )
        .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
            .maximumSessions(1) // 한 명의 유저는 하나의 세션만 가질 수 있음
            .maxSessionsPreventsLogin(false)
        )
        .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
      throws Exception {
    return config.getAuthenticationManager();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
