package webProject.togetherPartyTonight;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class TogetherPartyTonightApplication {
	public static final String APPLICATION_LOCATIONS = "spring.config.location="
			+ "classpath:application.yml,"
			+ "classpath:application-local.yml,"
			+ "classpath:application-prod.yml,"
			+ "classpath:aws.yml";

	public static void main(String[] args) {
		new SpringApplicationBuilder(TogetherPartyTonightApplication.class)
				.properties(APPLICATION_LOCATIONS)
				.run(args);

	}

}
