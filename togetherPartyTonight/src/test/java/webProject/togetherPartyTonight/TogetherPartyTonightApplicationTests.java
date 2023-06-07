package webProject.togetherPartyTonight;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(
		properties = "spring.config.location=classpath:/application-test.yml,classpath:/aws-test.yml"
)
class TogetherPartyTonightApplicationTests {

	@Test
	void contextLoads() {
	}

}
