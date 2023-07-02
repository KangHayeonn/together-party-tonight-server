package webProject.togetherPartyTonight;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import webProject.togetherPartyTonight.global.config.SwaggerConfig;


@SpringBootTest(
		properties = "spring.config.location=classpath:/application-test.yml,classpath:/aws-test.yml"
)
@SpringJUnitConfig(SwaggerConfig.class)
class TogetherPartyTonightApplicationTests {

	@Test
	void contextLoads() {
	}

}
