package com.findToilet.global.config;

import com.findToilet.domain.member.repository.MemberRepository;
import com.findToilet.global.auth.handler.login.MemberAuthenticationEntryPoint;
import com.findToilet.global.auth.handler.logout.MemberLogoutHandler;
import com.findToilet.global.auth.handler.logout.MemberLogoutSuccessHandler;
import com.findToilet.global.auth.handler.oauth.OAuth2SuccessHandler;
import com.findToilet.global.auth.jwt.DelegateTokenUtil;
import com.findToilet.global.auth.jwt.JwtTokenizer;
import com.findToilet.global.auth.userdetails.CustomOAuth2UserService;
import com.findToilet.global.auth.utils.AccessTokenRenewalUtil;
import com.findToilet.global.auth.utils.OAuth2TokenUtils;
import com.findToilet.global.util.RedisUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity(debug = false)
public class SecurityConfig {

    private final JwtTokenizer jwtTokenizer;
    private final AccessTokenRenewalUtil accessTokenRenewalUtil;
    private final DelegateTokenUtil delegateTokenUtil;
    private final CustomOAuth2UserService oAuth2UserService;
    private final MemberRepository memberRepository;
    private final RedisUtils redisUtils;
    private final OAuth2TokenUtils oAuth2TokenUtils;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .headers().frameOptions().sameOrigin()
                .and()
                .csrf().disable()
                .cors(withDefaults())
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin().disable()
                .logout()
                .logoutUrl("/api/members/logout")
                .deleteCookies("Refresh")
                .addLogoutHandler(new MemberLogoutHandler(redisUtils, jwtTokenizer))
                .logoutSuccessHandler(new MemberLogoutSuccessHandler())
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new MemberAuthenticationEntryPoint())
                .and()
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint()
                        .userService(oAuth2UserService)
                        .and()
                        .successHandler(
                                new OAuth2SuccessHandler(delegateTokenUtil, memberRepository, jwtTokenizer,
                                        oAuth2TokenUtils)))
                .apply(customFilterConfigurers())
                .and()
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().permitAll());

        return http.build();
    }

    @Bean
    public CustomFilterConfig customFilterConfigurers() {
        return new CustomFilterConfig(jwtTokenizer, delegateTokenUtil, accessTokenRenewalUtil, redisUtils);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.setAllowedOrigins(
                List.of("http://localhost:8000"));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setExposedHeaders(List.of("*"));
        configuration.addExposedHeader("Authorization");
        configuration.addExposedHeader("Refresh");
        configuration.addExposedHeader("Location");
        configuration.addExposedHeader("Set-Cookie");
        configuration.addAllowedHeader("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
