package webProject.togetherPartyTonight.test;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.formLogin().disable()
                .httpBasic().disable()
                        .csrf().disable();
        http.authorizeRequests().anyRequest().permitAll();

        return http.build();
    }
}
