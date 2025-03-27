package study.issue_mate.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import study.issue_mate.jwt.CustomLogoutFilter;
import study.issue_mate.jwt.JWTFilter;
import study.issue_mate.jwt.JWTProvider;
import study.issue_mate.jwt.LoginFilter;
import study.issue_mate.util.RedisUtil;

import java.util.Collections;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Value("${next_public_base_url}")
    private String next_public_base_url;

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JWTProvider jwtProvider;
    private final RedisUtil redisUtil;
//    private final AuthRepository authRepository; // 추후 제거

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors((cors) -> cors.configurationSource(corsConfigurationSource()));

        http.csrf((auth) -> auth.disable());
        http.httpBasic((auth) -> auth.disable());
        http.formLogin((auth) -> auth.disable());
        http.logout((auth) -> auth.disable());

        http.authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/health/**").permitAll()
                        .requestMatchers("/login").permitAll()
                        .requestMatchers("/reissue", "/refreshCheck").permitAll()
//                .requestMatchers("/error").permitAll()
                        .anyRequest().authenticated()
        );
//        http.addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtProvider, authRepository, cartRepository), UsernamePasswordAuthenticationFilter.class);
        http.addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtProvider, redisUtil), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(new JWTFilter(jwtProvider), LoginFilter.class);
//        http.addFilterBefore(new CustomLogoutFilter(jwtProvider, authRepository), LogoutFilter.class);
        http.addFilterBefore(new CustomLogoutFilter(jwtProvider, redisUtil), LogoutFilter.class);

        // 인증되지 않은 사용자가 보호된 리소스에 액세스
//        http.exceptionHandling((auth)-> auth.authenticationEntryPoint(new CustomAuthenticationEntryPoint()));
        http.exceptionHandling((auth) -> {
            auth.authenticationEntryPoint((request, response, authException) -> {
                response.setStatus(401);
            });
        });

        // jwt 방식은 STATELESS 방식
        http.sessionManagement((session) -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration corsConfiguration = new CorsConfiguration();

        corsConfiguration.setAllowedOrigins(Collections.singletonList(next_public_base_url));
        corsConfiguration.setAllowedMethods(Collections.singletonList("*"));
        corsConfiguration.setAllowedHeaders(Collections.singletonList("*"));
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setMaxAge(3600L);
        corsConfiguration.addExposedHeader("Authorization");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration); // 모든 경로에 대해서 CORS 설정을 적용

        return source;
    }
}
