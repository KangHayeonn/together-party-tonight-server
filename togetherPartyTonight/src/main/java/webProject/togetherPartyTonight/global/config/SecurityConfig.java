package webProject.togetherPartyTonight.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import webProject.togetherPartyTonight.domain.member.auth.jwt.JwtAuthenticationEntryPoint;
import webProject.togetherPartyTonight.domain.member.auth.jwt.JwtFilter;
import webProject.togetherPartyTonight.domain.member.auth.jwt.JwtProvider;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtProvider jwtProvider;

    private final JwtAuthenticationEntryPoint authenticationEntryPoint;

    private static final String[] PUBLIC_GET = {"/reviews/{reviewId:^[0-9]*$}","/clubs/{clubId:^[0-9]*$}","/members/reviews/**","/search/**", "/comment/**", "/clubs/reviews/{clubId:^[0-9]*$}"};

    private static final String[] PUBLIC_POST = {"/members/emailCheck","/members/login","/members/reissue","/members/oauth/**","/members/signup/**","/members/password/**"};



    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.httpBasic().disable()  // 비인증시 login form redirect X (rest api)
                    .csrf().disable()       // csrf 보안 X (rest api)
                    .formLogin().disable()
                    .logout().disable()
                .cors()
                .and()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // jwt token으로 인증 > 세션 필요없음
                .and()
                    .addFilterBefore(new JwtFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class)
                    .exceptionHandling()
                    .authenticationEntryPoint(authenticationEntryPoint)
                .and()
                    .authorizeRequests(auth->auth
                            .mvcMatchers(HttpMethod.POST,PUBLIC_POST).permitAll()
                            .mvcMatchers(HttpMethod.GET,PUBLIC_GET).permitAll()
                        .anyRequest().authenticated()); // Authentication 필요한 주소


        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration
    ) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers("/v2/api-docs", "/swagger-resources/**", "/swagger-ui/index.html", "/swagger-ui.html","/webjars/**", "/swagger/**",
                "/h2-console/**","/configuration/security","/chatting/**","/**/chatting/","/");
    }



    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }






}
