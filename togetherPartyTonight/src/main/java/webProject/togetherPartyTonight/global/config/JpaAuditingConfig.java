package webProject.togetherPartyTonight.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
public class JpaAuditingConfig {
    /**
     * test 환경에서도 jpa auditing을 가능하게 하기 위해 configuration을 따로 만듬
     */
}
